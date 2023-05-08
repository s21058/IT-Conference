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
public class ParticipantServiceImpl implements ParticipantService {

    final private
    ParticipantRepository participantRepository;
    final private ConferenceRepository conferenceRepository;

    /**
     * @return participants, that are present in DataBase
     */
    public List<Participant> findAll() {
        return participantRepository.findAll();
    }

    /**
     * Validates and saves the participant object to the repository.
     *
     * @param participant the participant object to be saved
     * @return a response entity with an appropriate status code and message based on the validation results
     * - if the participant with the same login already exists, returns a bad request status with an error message
     * - if the participant with the same email and login already exists, returns a bad request status with an error message
     * - if the participant with the same email but different login already exists, returns a bad request status with an error message
     * - if the email is not registered, saves the participant and returns an ok status with a success message
     * - if an internal server error occurs, returns an internal server error status without a message
     */
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
                // Participant with such login and email already exists
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

    /**
     * Updates the email of the participant with the given id.
     *
     * @param id    the id of the participant to update
     * @param email the new email to set for the participant
     * @return a response entity with an appropriate status code and message based on the update results
     * - if the new email is the same as the current email, returns a bad request status with an error message
     * - if the new email already exists in the repository, returns a bad request status with an error message
     * - if the email is successfully updated, returns an ok status with a success message
     */
    @Override
    public ResponseEntity<?> updateEmail(Integer id, String email) {
        var participant = participantRepository.findById(id);
        // Check if current email is the same as new email
        if (participant.getEmail().equals(email)) {
            return ResponseEntity.badRequest().body("Your new email and current email the same");
            // Check if this email is already registered
        } else if (participantRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("This email already exist");
        }
        participant.setEmail(email);
        participantRepository.save(participant);
        return ResponseEntity.ok("Mail successfully changed");
    }

    /**
     * Cancels the reservation of a participant with the given login to a lecture with the given ID.
     *
     * @param login     the login of the participant to cancel the reservation for
     * @param lectureId the ID of the lecture to cancel the reservation for
     * @return a response entity with an appropriate status code and message based on the cancellation results
     * - if the participant or the lecture is not found, returns a not found response with an error message
     * - if the participant doesn't have a reservation for the lecture, returns a bad request status with an error message
     * - if the reservation is successfully cancelled, returns an ok status with a success message
     */
    @Transactional
    @Override
    public ResponseEntity<?> cancelReservation(String login, Integer lectureId) {
        var participant = participantRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("Participant not found"));
        var lecture = conferenceRepository.findById(lectureId);
        // Check if lecture exists in Participant reservations
        if (participant.checkLecture(lecture)) {
            participant.deleteLecture(lecture);
            lecture.deleteParticipant(participant);
        } else {
            return ResponseEntity.badRequest().body("You don't have or already canceled reservation to this lecture");
        }
        return ResponseEntity.ok("Reservation was removed");
    }

    /**
     * Retrieves the participant with the given login from the repository.
     *
     * @param login the login of the participant to retrieve
     * @return an optional containing the participant with the given login, or an empty optional if not found
     */
    @Override
    public Optional<Participant> findByLogin(String login) {
        return participantRepository.findByLogin(login);
    }

    /**
     * Retrieves the participant with the given email from the repository.
     *
     * @param email the email of the participant to retrieve
     * @return an optional containing the participant with the given email, or an empty optional if not found
     */
    @Override
    public Optional<Participant> findByEmail(String email) {
        return participantRepository.findByEmail(email);
    }

    /**
     * Retrieves the participant with the given ID from the repository.
     *
     * @param id the ID of the participant to retrieve
     * @return the participant with the given ID, or null if not found
     */
    @Override
    public Participant findById(Integer id) {
        return participantRepository.findById(id);
    }

    /**
     * Show plan of conference
     *
     * @return plan of conference
     */
    @Override
    public String showPlan() {
        return "Plan of the conference 26th of April :\n" +
                "Three lectures that will run simultaneously from 10:00 to 15:45 with 15 minutes coffee breaks.\n" +
                "At our conference you could find those 3 ways to participate: Java,.NET,Azure Cloud.\n" +
                "-First round of lectures starts at 10:00 and ends at 11:45\n" +
                "-Second round of lectures starts at 12:00 and ends at 13:45\n" +
                "-Third round of lectures starts at 14:00 and ends at 15:45\n";
    }
}
