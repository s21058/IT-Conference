package com.example.itconference.DTO;

import com.example.itconference.DTO.Participant.ParticipantGetDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


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
