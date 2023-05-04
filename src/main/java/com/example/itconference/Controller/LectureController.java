package com.example.itconference.Controller;

import com.example.itconference.DTO.LectureDTO;
import com.example.itconference.DTO.Participant.ParticipantReservationDTO;
import com.example.itconference.Model.Lecture;
import com.example.itconference.Service.ConferenceService.ConferenceService;
import com.example.itconference.Service.ParticipantService.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

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
