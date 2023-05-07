package com.example.itconference.Controller;

import com.example.itconference.Service.Organizer.OrganizerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/organizer")
public class OrganizerController {

private final OrganizerService organizerService;
    @GetMapping("/lecture-percentage")
    public ResponseEntity<?>lecturePercentage(@RequestParam Integer lectureId){
        return organizerService.interestInLecture(lectureId);
    }
    @GetMapping("/total-percentage")
    public ResponseEntity<?>total(){
        return ResponseEntity.ok().body(organizerService.interestInConference());
    }
}
