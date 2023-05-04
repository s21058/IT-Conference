package com.example.itconference.Controller;

import com.example.itconference.DTO.LectureDTO;
import com.example.itconference.DTO.ParticipantReservationDTO;
import com.example.itconference.DTO.ParticipantRegistrationDTO;
import com.example.itconference.Model.Lecture;
import com.example.itconference.Service.ParticipantService.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    final
    ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> register(@Valid @RequestBody ParticipantRegistrationDTO participantRegistrationDTO) {
        return ResponseEntity.ok(participantService.save(participantRegistrationDTO));
    }
    @GetMapping("/{login}/reservations")
    public List<LectureDTO> showReservations(@PathVariable String login){
        return Lecture.parseToDTO(participantService.findByLogin(login).get().getLectures());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantReservationDTO> findParticipant(@PathVariable Integer id){
        return null;
    }
}

