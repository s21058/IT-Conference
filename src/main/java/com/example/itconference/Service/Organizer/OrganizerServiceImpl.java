package com.example.itconference.Service.Organizer;

import com.example.itconference.Model.Lecture;
import com.example.itconference.Repository.ConferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizerServiceImpl implements OrganizerService {
    private final ConferenceRepository conferenceRepository;

    @Override
    public ResponseEntity<?> interestInLecture(Integer id) {
        if(!conferenceRepository.existsById(Long.valueOf(id))){
            return ResponseEntity.badRequest().body("This lecture doesn't exist");
        }
        double percentage=((double) conferenceRepository.findById(id).getParticipants().size()/Lecture.MAX_PARTICIPANTS)*100;
        return ResponseEntity.ok().body("Percentage of participants in a given lecture is  "+percentage+"%");
    }
}
