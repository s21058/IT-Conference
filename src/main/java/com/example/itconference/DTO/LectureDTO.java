package com.example.itconference.DTO;

import com.example.itconference.Model.Lecture;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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


}
