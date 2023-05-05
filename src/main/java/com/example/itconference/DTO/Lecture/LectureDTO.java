package com.example.itconference.DTO.Lecture;

import com.example.itconference.DTO.Participant.ParticipantGetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class LectureDTO {
    @Getter
    @Setter
    private String topic;

    @Getter
    @Setter
    private LocalTime startTime;

    @Getter
    @Setter
    private LocalTime endTime;

    @Getter
    @Setter
    private List<ParticipantGetDTO>participants=new ArrayList<>();
}
