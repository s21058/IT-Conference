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
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ConferenceServiceImpl implements ConferenceService {

    final private
    ConferenceRepository conferenceRepository;
    final private ParticipantRepository participantRepository;

    /**
     * @return lectures, that are present in DataBase
     */
    @Override
    public List<Lecture> findAll() {
        return conferenceRepository.findAll();
    }
    /**
     * Saves a lecture to the database if it meets the criteria for a valid lecture.

     * @param lecture The Lecture object to be saved.

     * @return A ResponseEntity with a String message indicating success or failure of the operation.
     */
    @Transactional
    @Override
    public ResponseEntity<String> saveLecture(Lecture lecture) {
        Map<LocalTime, LocalTime> validTime = Map.of(
                LocalTime.of(10, 0), LocalTime.of(11, 45),
                LocalTime.of(12, 0), LocalTime.of(13, 45),
                LocalTime.of(14, 0), LocalTime.of(15, 45));
        List<String> allowedTopic = new ArrayList<>(Arrays.asList("Java", ".NET", "Azure Cloud"));
        // Check if lecture has valid time
        if (!isValidTime(lecture, validTime)) {
            return ResponseEntity.badRequest().body("Invalid time for lecture. Allowed times: 10:00-11:45, 12:00-13:45, 14:00-15:45.");
        }
        // Check if topic has name that was preregistered in conference plan
        if (!isTopicValid(allowedTopic, lecture.getTopic())) {
            var lectures = conferenceRepository.findAll();
            // Check if there are already 3 lectures at the same time
            if (isThreeAtTheSameTime(lectures, lecture.getStartTime())) {
                return ResponseEntity.badRequest().body("It's possible to add only 3 lectures at the same time");
            }
            return ResponseEntity.badRequest().body("You cannot add Topic[" + lecture.getTopic() + "]. Available Topics are Java, .NET ,Azure Cloud ");
        }
        List<Lecture> lectures = conferenceRepository.findByTopic(lecture.getTopic());
        // Check if this lecture already present in DataBase
        if (isLecturePresent(lectures, lecture)) {
            return ResponseEntity.badRequest().body("Lecture with topic " + lecture.getTopic() + " and time " + lecture.getStartTime() + "-" + lecture.getEndTime() + " already exists.");
        }
        conferenceRepository.save(lecture);
        return ResponseEntity.ok("Lecture was successfully added to database");
    }

    /**
     * Retrieves the lecture with the given ID from the repository.
     *
     * @param id the ID of the lecture to retrieve
     * @return the lecture with the given ID, or null if not found
     */
    @Override
    public Lecture findById(Integer id) {
        return conferenceRepository.findById(id);
    }

    /**
     * Retrieves the lecture with the given topic from the repository.
     *
     * @param topic the topic of the lecture to retrieve
     * @return collection of lectures with the same topic
     */
    @Override
    public List<Lecture> findByTopic(String topic) {
        return conferenceRepository.findByTopic(topic);
    }
    /**
     *This method allows a participant to make a reservation for a lecture.
     * It takes an Integer ID of the lecture and ParticipantReservationDTO as parameters.

     * @param idLecture Integer ID of the lecture
     * @param participantInfo ParticipantReservationDTO containing information about the participant who wants to make a reservation
     * @return ResponseEntity object containing a success message if the reservation was made successfully,
     * or an error message if the lecture is already full, or if the participant is already registered for the lecture or has a reservation at the same time.
     * @throws IOException If an I/O error occurs while writing to the file
     */
    @Override
    @Transactional
    public ResponseEntity<?> makeReservation(Integer idLecture, ParticipantReservationDTO participantInfo) throws IOException {
        var lecture = conferenceRepository.findById(idLecture);
        var participant = participantRepository.findByLogin(participantInfo.getLogin()).get();
        // Check if lecture already have 5 Participants
        if (lecture.isFull()) {
            return ResponseEntity.badRequest().body("This lecture already has the maximum number of participants, try to register for another time");
        }
        // Check if this login already registered but not the email
        if (participantRepository.findAll().stream().anyMatch(p -> p.getLogin().equals(participantInfo.getLogin()) &&
                !p.getEmail().equals(participantInfo.getEmail()))) {
            return ResponseEntity.badRequest().body("This login[" + participantInfo.getLogin() + "] already used");
        // Check if Participant already registered to this lecture
        } else if (participant.getLectures().stream().anyMatch(l -> l.getTopic().equals(lecture.getTopic()) &&
                l.getStartTime().compareTo(lecture.getStartTime()) == 0)) {
            return ResponseEntity.badRequest().body("You have already been registered for this lecture");
        // Check if Participant have reservations at this time
        } else if (participant.getLectures().stream().anyMatch(l -> l.getStartTime().compareTo(lecture.getStartTime()) == 0)) {
            return ResponseEntity.badRequest().body("You already have reservation at " + lecture.getStartTime());
        }
        lecture.addParticipant(participant);
        participant.addLecture(lecture);
        writeToFile(participant.getEmail(), lecture.getStartTime(), lecture.getTopic());
        return ResponseEntity.ok("You successfully registered to this lecture " + lecture.getTopic() + " on 26th of April at " + lecture.getStartTime() + "-" + lecture.getEndTime());
    }

    /**
     * Returns a response entity containing either a list of all registered participants or an error message.
     * @param login a string representing the login of the user making the request
     *              -if it differ from 'admin' , return error
     *              -if it equals to 'admin', return ok status
     * @return a response entity containing a list of registered participants or an error message
     */
    @Override
    public ResponseEntity<?> findRegistered(String login) {
        // Check if login equals to admin
        if (login.equals("admin")) {
            return ResponseEntity.ok().body(Participant.toDTOinSystem(allRegistered()));
        } else {
            return ResponseEntity.badRequest().body("You dont have permission for this operation");
        }
    }

    /**
     *
     * @param lecture representing lecture that we want to add to DataBase
     * @param validTime representing map,where on key we have startTime, and in value endTime
     * @return true if lecture has the same as validTime,otherwise false
     */
    private boolean isValidTime(Lecture lecture, Map<LocalTime, LocalTime> validTime) {
        return validTime.entrySet()
                .stream()
                .anyMatch(entry -> entry.getKey().equals(lecture.getStartTime()) && entry.getValue().equals(lecture.getEndTime()));
    }

    /**
     *
     * @param lectures representing all lectures from database
     * @param lecture represent lecture that we want to add
     * @return true if lectures don't contains such lecture,otherwise false
     */
    private boolean isLecturePresent(List<Lecture> lectures, Lecture lecture) {
        return lectures.stream().anyMatch(
                l -> l.getStartTime().compareTo(lecture.getStartTime()) == 0);
    }

    /**
     *
     * @param email represents email,that will receive response
     * @param startTime represents start time of the lecture, which participant submitted to
     * @param topic represents name of topic participant submitted to
     * @throws IOException if file don't exist
     */
    private void writeToFile(String email, LocalTime startTime, String topic) throws IOException {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ", " + email + ", Hello,We are appreciate that you decided to participate in our " + topic + " lecture! See you April 26th at " + startTime + "\n";
        Resource resource = new FileSystemResource("powiadomienia.txt");
        FileWriter fileWriter = new FileWriter(resource.getFile(), true);
        fileWriter.write(data);
        fileWriter.close();
    }

    /**
     *
     * @param topics representing topics that should be in DataBase
     * @param topic represent name of topic that we want to add
     * @return true if this topic name is in our topics collection,otherwise false
     */
    private boolean isTopicValid(List<String> topics, String topic) {
        return topics.stream().anyMatch(t -> t.equals(topic));
    }

    /**
     * @param lectures representing all lectures in DataBase
     * @param start representing start time for lectures
     * @return true if there are not 3 lectures with the same start time,otherwise return false
     */
    private boolean isThreeAtTheSameTime(List<Lecture> lectures, LocalTime start) {
        var count = lectures.stream().map(l -> l.getStartTime().compareTo(start) == 0).count();
        return count == 3;
    }

    /**
     * This method looking for Participants that have at least 1 reservation
     * @return Unique participants with ,at least, 1 reservation
     */
    private List<Participant> allRegistered() {
        var lectures = conferenceRepository.findAll().stream().
                filter(l -> l.getParticipants().size() > 0).collect(Collectors.toList());
        return lectures.stream().
                flatMap(lecture -> lecture.getParticipants().stream()).distinct().collect(Collectors.toList());
    }
}
