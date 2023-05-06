package com.example.itconference.Service.ParticipantService;

import com.example.itconference.Model.Participant;
import com.example.itconference.Repository.ConferenceRepository;
import com.example.itconference.Repository.ParticipantRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ParticipantServiceImpl implements ParticipantService{

    final private
    ParticipantRepository participantRepository;
    final private ConferenceRepository conferenceRepository;

    public List<Participant> findAll() {
        return participantRepository.findAll();
    }
    @Override
    public ResponseEntity<String> save(Participant participant) {
        if (participantRepository.existsByLogin(participant.getLogin())) {
            // Participant with the same login already exists
            return ResponseEntity.badRequest().body("Participant with such login [" + participant.getLogin() + "] already exists");
        }

        switch (participantRepository.countByEmail(participant.getEmail())) {
            case 0:
                // Email is not registered, save the participant
                participantRepository.save(participant);
                return ResponseEntity.ok("Participant saved");
            case 1:
                var existingParticipant = participantRepository.findByEmail(participant.getEmail()).get();
                if (existingParticipant.getLogin().equals(participant.getLogin())) {
                    // Participant with the same email and login already exists
                    return ResponseEntity.badRequest().body("This email [" + participant.getEmail() + "] and login [" + participant.getLogin() + "] are already registered");
                } else {
                    // Participant with the same email but different login already exists
                    return ResponseEntity.badRequest().body("This email [" + participant.getEmail() + "] is already registered with a different login");
                }
            default:
                // This should not happen
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<?> updateEmail(Integer id, String email) {
        var participant=participantRepository.findById(id);
        if(participant.getEmail().equals(email)){
            return ResponseEntity.badRequest().body("Your new email and current email the same");
        }else if(participantRepository.existsByEmail(email)){
            return ResponseEntity.badRequest().body("This email already exist");
        }
        participant.setEmail(email);
        participantRepository.save(participant);
        return ResponseEntity.ok("Mail successfully changed");
    }

    @Transactional
    @Override
    public ResponseEntity<?> cancelReservation(String login,Integer lectureId) {
        var participant= participantRepository.findByLogin(login).orElseThrow(()->new RuntimeException("Participant not found"));
        var lecture=conferenceRepository.findById(lectureId);
        participant.deleteLecture(lecture);
        lecture.deleteParticipant(participant);
        return ResponseEntity.ok("Reservation was removed");
    }

    @Override
    public Optional<Participant> findByLogin(String login) {
        return participantRepository.findByLogin(login);
    }

    @Override
    public Optional<Participant> findByEmail(String email) {
        return participantRepository.findByEmail(email);
    }

    @Override
    public Participant findById(Integer id) {
        return participantRepository.findById(id);
    }

}
