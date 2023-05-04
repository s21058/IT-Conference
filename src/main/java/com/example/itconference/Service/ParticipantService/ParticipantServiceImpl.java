package com.example.itconference.Service.ParticipantService;

import com.example.itconference.DTO.ParticipantRegistrationDTO;
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

    public List<Participant> findAll() {
        return participantRepository.findAll();
    }

    public ResponseEntity<String> save(ParticipantRegistrationDTO participantRegistrationDTO) {
        var existingParticipant = participantRepository.findByLogin(participantRegistrationDTO.getLogin());

        if (existingParticipant.isPresent() && !existingParticipant.get().getEmail().equals(participantRegistrationDTO.getEmail())) {
            // Participant with the same login but different email already exists
            return ResponseEntity.badRequest().body("Participant with such login [" + participantRegistrationDTO.getLogin() + "] already exists");
        } else if (existingParticipant.isPresent() && existingParticipant.get().getEmail().equals(participantRegistrationDTO.getEmail())) {
            // Participant with the same login and email already exists
            return ResponseEntity.badRequest().body("This email [" + participantRegistrationDTO.getEmail() + "] and login[" + participantRegistrationDTO.getLogin() + "] are already registered");
        }else if(isEmailExist(participantRegistrationDTO.getEmail())){
            return ResponseEntity.badRequest().body("This email already exist");
        } else {
            // Participant does not exist, save it
            participantRepository.save(participantRegistrationDTO.toParticipant());
            return ResponseEntity.ok("Saved");
        }
    }

    @Override
    public Optional<Participant> findByLogin(String login) {
        return participantRepository.findByLogin(login);
    }

    @Override
    public Optional<Participant> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Participant findById(Integer id) {
        return participantRepository.findById(id);
    }


    protected boolean isEmailExist(String email){
        return participantRepository.findAll().stream().anyMatch(participant -> participant.getEmail().equals(email));
    }
}
