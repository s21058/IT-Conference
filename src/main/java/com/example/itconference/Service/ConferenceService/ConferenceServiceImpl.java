package com.example.itconference.Service.ConferenceService;

import com.example.itconference.DTO.Participant.ParticipantReservationDTO;
import com.example.itconference.Model.Lecture;
import com.example.itconference.Model.Participant;
import com.example.itconference.Repository.ConferenceRepository;
import com.example.itconference.Repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ConferenceServiceImpl implements ConferenceService {

    final private
    ConferenceRepository conferenceRepository;
    final private ParticipantRepository participantRepository;


    @Override
    public List<Lecture> findAll() {
        return conferenceRepository.findAll();
    }

    @Override
    public ResponseEntity<String> saveLecture(Lecture lecture) {
        Map<LocalTime, LocalTime> validTime = Map.of(
                LocalTime.of(10, 0), LocalTime.of(11, 45),
                LocalTime.of(12, 0), LocalTime.of(13, 45),
                LocalTime.of(14, 0), LocalTime.of(15, 45));
        List<String> allowedTopic = new ArrayList<>(Arrays.asList("Java", ".NET", "Azure Cloud"));
        if (!isValidTime(lecture, validTime)) {
            return ResponseEntity.badRequest().body("Invalid time for lecture. Allowed times: 10:00-11:45, 12:00-13:45, 14:00-15:45.");
        }
        if (!isTopicValid(allowedTopic, lecture.getTopic())) {
            var lectures = conferenceRepository.findAll();
            if (isThreeAtTheSameTime(lectures, lecture.getStartTime())) {
                return ResponseEntity.badRequest().body("It's possible to add only 3 lectures at the same time");
            }
            return ResponseEntity.badRequest().body("You cannot add Topic[" + lecture.getTopic() + "]. Available Topics are Java, .NET ,Azure Cloud ");
        }
        List<Lecture> lectures = conferenceRepository.findByTopic(lecture.getTopic());
        if (isLecturePresent(lectures, lecture)) {
            return ResponseEntity.badRequest().body("Lecture with topic " + lecture.getTopic() + " and time " + lecture.getStartTime() + "-" + lecture.getEndTime() + " already exists.");
        }
        conferenceRepository.save(lecture);
        return ResponseEntity.ok("Lecture was successfully added to database");
    }

    @Override
    public Lecture findById(Integer id) {
        return conferenceRepository.findById(id);
    }

    @Override
    public List<Lecture> findByTopic(String topic) {
        return conferenceRepository.findByTopic(topic);
    }

    @Override
    @Transactional
    public ResponseEntity<?> makeReservation(Integer idLecture, ParticipantReservationDTO participantInfo) throws IOException {
        var lecture = conferenceRepository.findById(idLecture);
        var participant = participantRepository.findByLogin(participantInfo.getLogin()).get();
        if (lecture.isFull()) {
            return ResponseEntity.badRequest().body("This lecture already has the maximum number of participants, try to register for another time");
        }
        if (participantRepository.findAll().stream().anyMatch(p -> p.getLogin().equals(participantInfo.getLogin()) &&
                !p.getEmail().equals(participantInfo.getEmail()))) {
            return ResponseEntity.badRequest().body("This login[" + participantInfo.getLogin() + "] already used");

        } else if (participant.getLectures().stream().anyMatch(l -> l.getTopic().equals(lecture.getTopic()) &&
                l.getStartTime().compareTo(lecture.getStartTime()) == 0)) {
            return ResponseEntity.badRequest().body("You have already been registered for this lecture");

        } else if (participant.getLectures().stream().anyMatch(l -> l.getStartTime().compareTo(lecture.getStartTime()) == 0)) {
            return ResponseEntity.badRequest().body("You already have reservation at " + lecture.getStartTime());
        }
        lecture.addParticipant(participant);
        participant.addLecture(lecture);
        writeToFile(participant.getEmail(), lecture.getStartTime(), lecture.getTopic());
        return ResponseEntity.ok("You successfully registered to this lecture " + lecture.getTopic() + " on 26th of April at " + lecture.getStartTime() + "-" + lecture.getEndTime());
    }

    @Override
    public ResponseEntity<?> findRegistered(String login) {
        if (login.equals("admin")) {
            return ResponseEntity.ok().body(Participant.toDTOinSystem(allRegistered()));
        } else {
            return ResponseEntity.badRequest().body("You dont have permission for this operation");
        }
    }


    private boolean isValidTime(Lecture lecture, Map<LocalTime, LocalTime> validTime) {
        return validTime.entrySet()
                .stream()
                .anyMatch(entry -> entry.getKey().equals(lecture.getStartTime()) && entry.getValue().equals(lecture.getEndTime()));
    }

    private boolean isLecturePresent(List<Lecture> lectures, Lecture lecture) {
        return lectures.stream().anyMatch(
                l -> l.getStartTime().compareTo(lecture.getStartTime()) == 0);
    }

    private void writeToFile(String email, LocalTime startTime, String topic) throws IOException {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ", " + email + ", Hello,We are appreciate that you decided to participate in our " + topic + " lecture! See you April 26th at " + startTime + "\n";
        Resource resource = new FileSystemResource("powiadomienia.txt");
        FileWriter fileWriter = new FileWriter(resource.getFile(), true);
        fileWriter.write(data);
        fileWriter.close();
    }

    private boolean isTopicValid(List<String> topics, String topic) {
        return topics.stream().anyMatch(t -> t.equals(topic));
    }

    private boolean isThreeAtTheSameTime(List<Lecture> lectures, LocalTime start) {
        var count = lectures.stream().map(l -> l.getStartTime().compareTo(start) == 0).count();
        return count == 3;
    }

    private List<Participant> allRegistered() {
        var lectures = conferenceRepository.findAll().stream().
                filter(l -> l.getParticipants().size() > 0).collect(Collectors.toList());
        return lectures.stream().
                flatMap(lecture -> lecture.getParticipants().stream())
                .collect(Collectors.toList());
    }
}
