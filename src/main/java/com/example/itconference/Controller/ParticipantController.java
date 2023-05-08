package com.example.itconference.Controller;

import com.example.itconference.DTO.Lecture.LectureGetDTO;
import com.example.itconference.DTO.Participant.ParticipantGetDTO;
import com.example.itconference.Model.Lecture;
import com.example.itconference.Model.Participant;
import com.example.itconference.Service.ParticipantService.ParticipantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

@RestController
@RequestMapping("/participant")
@AllArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/registration")
    public ResponseEntity<?> register(@Valid @RequestBody Participant participant) {
        return ResponseEntity.ok(participantService.save(participant));
    }
    @PutMapping("/changeEmail")
    public ResponseEntity<?>changeEmail(@RequestParam Integer id,@Valid @Email @RequestParam String email){
        return participantService.updateEmail(id,email);
    }
    @GetMapping("/reservations")
    public List<LectureGetDTO> showReservations(@RequestParam String login){
        return Lecture.parseToDTOList(participantService.findByLogin(login).get().getLectures());
    }

    @GetMapping("/find")
    public ResponseEntity<ParticipantGetDTO> findParticipant(@RequestParam Integer id){
        return ResponseEntity.ok(Participant.toDTO(participantService.findById(id)));
    }
    @DeleteMapping("/reservations/cancel")
    public ResponseEntity<?>cancelReservation(@RequestParam String login,@RequestParam Integer lectureId){
        return participantService.cancelReservation(login,lectureId);
    }

    @GetMapping("showPlan")
    public String showPlan(){
        return participantService.showPlan();
    }
}

