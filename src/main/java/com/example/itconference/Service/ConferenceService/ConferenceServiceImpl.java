package com.example.itconference.Service.ConferenceService;

import com.example.itconference.DTO.LectureDTO;
import com.example.itconference.DTO.Participant.ParticipantReservationDTO;
import com.example.itconference.Model.Lecture;
import com.example.itconference.Model.Participant;
import com.example.itconference.Repository.ConferenceRepository;
import com.example.itconference.Repository.ParticipantRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ConferenceServiceImpl implements ConferenceService {

    final
    ConferenceRepository conferenceRepository;
    final ParticipantRepository participantRepository;

    public ConferenceServiceImpl(ConferenceRepository conferenceRepository, ParticipantRepository participantRepository) {
        this.conferenceRepository = conferenceRepository;
        this.participantRepository = participantRepository;
    }

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

        if (!isValidTime(lecture, validTime)) {
            return ResponseEntity.badRequest().body("Invalid time for lecture. Allowed times: 10:00-11:45, 12:00-13:45, 14:00-15:45.");
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
    public ResponseEntity<?> makeReservation(Integer idLecture, ParticipantReservationDTO participantInfo) throws IOException {
        var lecture = Lecture.parseToDTO(conferenceRepository.findById(idLecture));
        var participant= Participant.toDTO(participantRepository.findByLogin(participantInfo.getLogin()));
        if(participantRepository.findAll().stream().anyMatch(p->p.getLogin().equals(participantInfo.getLogin())&&
                !p.getEmail().equals(participantInfo.getEmail()))){
            return ResponseEntity.badRequest().body("This login["+participantInfo.getLogin()+"] already used");
        }else if(participant.getLectures().stream().anyMatch(l->l.getTopic().equals(lecture.getTopic())&&
                l.getStartTime().compareTo(lecture.getStartTime())==0)){
            return ResponseEntity.badRequest().body("You have already been registered for this lecture");
        }
        participant.getLectures().add(lecture);
        lecture.getParticipants().add(participant);
        writeToFile("../powiadomienia.txt",participant.getEmail());
        return ResponseEntity.ok("You successfully registered to this lecture" + lecture.getTopic()+" on "+lecture.getStartTime()+"-"+lecture.getEndTime());
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

    private void writeToFile(String path,String email) throws IOException {
        String data=LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+", "+email+", Hello,We are appreciate that you decided to participate in our lectures! See you on";
        Resource resource=new FileSystemResource(path);
        FileWriter fileWriter=new FileWriter(resource.getFile(),true);
        fileWriter.write(data);
        fileWriter.close();
    }
}
