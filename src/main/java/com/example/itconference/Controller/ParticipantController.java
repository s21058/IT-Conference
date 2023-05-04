package com.example.itconference.Controller;

import com.example.itconference.DTO.ParticipantReservationDTO;
import com.example.itconference.DTO.ParticipantRegistrationDTO;
import com.example.itconference.Model.Lecture;
import com.example.itconference.Service.ParticipantService.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @GetMapping("/{login}/checkReservations")
    public List<Lecture> checkReservation(@PathVariable String login){

        return null;
    }
    @GetMapping("/all")
    public ResponseEntity<?>getAll(){
        return ResponseEntity.ok(participantService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ParticipantReservationDTO> findParticipant(@PathVariable Integer id){

        return null;
    }
}

