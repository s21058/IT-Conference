package com.example.itconference.Service.ConferenceService;

import com.example.itconference.DTO.Participant.ParticipantReservationDTO;
import com.example.itconference.Model.Lecture;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface ConferenceService {
    List<Lecture>findAll();
    ResponseEntity<String> saveLecture(Lecture lecture);
    Lecture findById(Integer id);
    List<Lecture> findByTopic(String topic);
    ResponseEntity<?>makeReservation(Integer idLecture, ParticipantReservationDTO reservationDTO);

}
