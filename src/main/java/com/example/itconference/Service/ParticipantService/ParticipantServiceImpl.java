package com.example.itconference.Service.ParticipantService;


import com.example.itconference.DTO.Participant.ParticipantGetDTO;
import com.example.itconference.DTO.Participant.ParticipantRegistrationDTO;
import com.example.itconference.Model.Participant;
import com.example.itconference.Repository.ParticipantRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantServiceImpl implements ParticipantService{

    final
    ParticipantRepository participantRepository;

    public ParticipantServiceImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<ParticipantGetDTO> findAll() {
        return Participant.toDTO(participantRepository.findAll());
    }

    public ResponseEntity<String> save(Participant participant) {
        var existingParticipant = participantRepository.findByLogin(participant.getLogin());

        if (existingParticipant.isPresent() && !existingParticipant.get().getEmail().equals(participant.getEmail())) {
            // Participant with the same login but different email already exists
            return ResponseEntity.badRequest().body("Participant with such login [" + participant.getLogin() + "] already exists");
        } else if (existingParticipant.isPresent() && existingParticipant.get().getEmail().equals(participant.getEmail())) {
            // Participant with the same login and email already exists
            return ResponseEntity.badRequest().body("This email [" + participant.getEmail() + "] and login[" + participant.getLogin() + "] are already registered");
        }else if(isEmailExist(participant.getEmail())){
            return ResponseEntity.badRequest().body("This email already exist");
        } else {
            // Participant does not exist, save it
            participantRepository.save(participant);
            // return string
            return ResponseEntity.ok("Saved");
        }
    }

    @Override
    public ResponseEntity<?> updateEmail(Integer id, String email) {
        var participant=participantRepository.findById(id);
        if(participant.getEmail().equals(email)){
            return ResponseEntity.badRequest().body("Your new email and current email the same");
        }else if(participantRepository.findByEmail(email).isPresent()){
            return ResponseEntity.badRequest().body("This email already exist");
        }
        participant.setEmail(email);
        participantRepository.save(participant);
        return ResponseEntity.ok("Mail successfully changed");
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


    protected boolean isEmailExist(String email){
        return participantRepository.findAll().stream().anyMatch(participant -> participant.getEmail().equals(email));
    }
}
