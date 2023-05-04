package com.example.itconference.Controller;

import com.example.itconference.DTO.LectureDTO;
import com.example.itconference.DTO.ParticipantReservationDTO;
import com.example.itconference.Model.Lecture;
import com.example.itconference.Service.ConferenceService.ConferenceService;
import com.example.itconference.Service.ParticipantService.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conference")
public class LectureController {
    @Autowired
    ConferenceService conferenceService;

    @Autowired
    ParticipantService participantService;

    @GetMapping("/lectures")
    private List<LectureDTO> getAllLectures() {

        return null;
    }

    @GetMapping("/lectures/lecture/{idLecture}")
    public ResponseEntity<LectureDTO> chooseLecture(@PathVariable Long idLecture) {

        return null;
    }

    //As a second parameter we can use Principal from Spring Security to identify authorized user
    @PostMapping("/lectures/lecture/{idLecture}/submit")
    public ResponseEntity<?> submitChoice(@PathVariable Long idLecture, @RequestBody ParticipantReservationDTO participantInfo) {
//        Participant participant = participantService.findByLogin(participantInfo.getLogin());
//        var participantDTO=ParticipantController.parseToDTO(participant);
//        Lecture lecture = conferenceService.findById(idLecture);
//        var checkParticipant = participant.getLectures().stream().map(
//                reservedLectures -> reservedLectures.getId().equals(lecture.getId())).findAny().get();
//        if (checkParticipant) {
//            return ResponseEntity.badRequest().body("You have already been registered for this lecture");
//        } else {
//            lecture.getParticipants().add(participantDTO);
//            participant.getLectures().add(lecture);
//            return ResponseEntity.ok("You successfully registered to this lecture" + lecture);
//        }
        return null;
    }

    @PostMapping("/lectures/addLecture")
    public ResponseEntity<?> addLecture(@RequestBody Lecture lecture) {
//        Map<LocalTime, LocalTime> validTime = Map.of(
//                LocalTime.of(10, 0), LocalTime.of(11, 45),
//                LocalTime.of(12, 0), LocalTime.of(13, 45),
//                LocalTime.of(14, 0), LocalTime.of(15, 45));
//
//        if (!isValidTime(lecture, validTime)) {
//            return ResponseEntity.badRequest().body("Invalid time for lecture. Allowed times: 10:00-11:45, 12:00-13:45, 14:00-15:45.");
//        }
//        List<Lecture> lectures = conferenceService.findByTopic(lecture.getTopic());
//        if (isLecturePresent(lectures, lecture)) {
//            return ResponseEntity.badRequest().body("Lecture with topic " + lecture.getTopic() + " and time " + lecture.getStartTime() + "-" + lecture.getEndTime() + " already exists.");
//        }
//        conferenceService.saveLecture(lecture);
//        return ResponseEntity.ok("Lecture was successfully added to database");
        return null;
    }

    protected List<LectureDTO> parseToDTO(List<Lecture> lectures) {
        return lectures.stream().map(lecture -> {
            LectureDTO dto = new LectureDTO();
            dto.setTopic(lecture.getTopic());
            dto.setStartTime(lecture.getStartTime());
            dto.setEndTime(lecture.getEndTime());
            return dto;
        }).collect(Collectors.toList());
    }

    protected LectureDTO parseToDTO(Lecture lecture) {
        LectureDTO dto = new LectureDTO();
        dto.setTopic(lecture.getTopic());
        dto.setStartTime(lecture.getStartTime());
        dto.setEndTime(lecture.getEndTime());
        return dto;
    }

    protected boolean isValidTime(Lecture lecture, Map<LocalTime, LocalTime> validTime) {
        return validTime.entrySet()
                .stream()
                .anyMatch(entry -> entry.getKey().equals(lecture.getStartTime()) && entry.getValue().equals(lecture.getEndTime()));
    }

    protected boolean isLecturePresent(List<Lecture>lectures,Lecture lecture){
        return lectures.stream().anyMatch(
                l->l.getStartTime().compareTo(lecture.getStartTime())==0);
    }
}
