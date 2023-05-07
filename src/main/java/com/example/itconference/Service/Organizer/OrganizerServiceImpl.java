package com.example.itconference.Service.Organizer;

import com.example.itconference.Model.Lecture;
import com.example.itconference.Repository.ConferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrganizerServiceImpl implements OrganizerService {
    private final ConferenceRepository conferenceRepository;

    @Override
    public ResponseEntity<?> interestInLecture(Integer id) {
        if (!conferenceRepository.existsById(Long.valueOf(id))) {
            return ResponseEntity.badRequest().body("This lecture doesn't exist");
        }
        double percentage = ((double) conferenceRepository.findById(id).getParticipants().size() / Lecture.MAX_PARTICIPANTS) * 100;
        return ResponseEntity.ok().body("Percentage of participants in a given lecture is  " + percentage + "%");
    }

    @Override
    public String interestInConference() {
        Set<String>topics=conferenceRepository.findAll().stream().map(Lecture::getTopic).collect(Collectors.toSet());
        String retString="";
        for (var topic:topics) {
            var numberOfParticipants=  conferenceRepository.findByTopic(topic).stream().mapToInt(lecture -> lecture.getParticipants().size()).sum();
            double percentage=((double) numberOfParticipants/multiplication(topic))*100;
            retString+="Percentage for "+topic+" is "+percentage+"%\n";
        }
        return retString;
    }
    private int multiplication(String topic){
        return conferenceRepository.findByTopic(topic).size()*Lecture.MAX_PARTICIPANTS;
    }
}
