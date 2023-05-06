package com.example.itconference.Service.ParticipantService;

import com.example.itconference.DTO.Participant.ParticipantGetDTO;
import com.example.itconference.Model.Participant;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


public interface ParticipantService {
    List<Participant> findAll();
    ResponseEntity<String> save(Participant participant);
    Optional<Participant> findByLogin(String login);
    Optional<Participant> findByEmail(String email);
    Participant findById(Integer id);
    ResponseEntity<?> updateEmail(Integer id, String email);
    ResponseEntity<?> cancelReservation(String login,Integer lectureId);
}
