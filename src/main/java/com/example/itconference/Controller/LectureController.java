package com.example.itconference.Controller;

import com.example.itconference.DTO.Lecture.LectureDTO;
import com.example.itconference.DTO.Participant.ParticipantReservationDTO;
import com.example.itconference.Model.Lecture;
import com.example.itconference.Service.ConferenceService.ConferenceService;
import lombok.AllArgsConstructor;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/conference")
@AllArgsConstructor
public class LectureController {
    private final ConferenceService conferenceService;
    @GetMapping("/registered")
    public ResponseEntity<?>showRegistered(@RequestParam String login){
        return conferenceService.findRegistered(login);
    }

    @GetMapping("/lectures")
    public List<LectureDTO> getAllLectures() {
     return Lecture.parseToDTO(conferenceService.findAll());
    }

    @GetMapping("/lectures/lecture/{idLecture}")
    public ResponseEntity<LectureDTO> chooseLecture(@PathVariable Integer idLecture) {
        return ResponseEntity.ok(Lecture.parseToDTO(conferenceService.findById(idLecture)));
    }

    @PostMapping("/lectures/lecture/{idLecture}/submit")
    public ResponseEntity<?> submitChoice(@PathVariable Integer idLecture, @RequestBody ParticipantReservationDTO participantInfo) throws IOException {
        return conferenceService.makeReservation(idLecture,participantInfo);
    }

    @PostMapping("/lectures/addLecture")
    public ResponseEntity<?> addLecture(@RequestBody Lecture lecture) {
        return conferenceService.saveLecture(lecture);
    }

}
