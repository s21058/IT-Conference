package com.example.itconference.Service.ParticipantService;

import com.example.itconference.DTO.Participant.ParticipantRegistrationDTO;
import com.example.itconference.Model.Participant;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


public interface ParticipantService {
    List<Participant> findAll();
    ResponseEntity<String> save(ParticipantRegistrationDTO participantRegistrationDTO);
    Optional<Participant> findByLogin(String login);
    Optional<Participant> findByEmail(String email);
    Participant findById(Integer id);
    ResponseEntity<?> updateEmail(Integer id, String email);
}
