package com.example.itconference.DTO;

import lombok.Data;

import java.time.LocalTime;

@Data
public class LectureDTO {
    private String topic;
    private LocalTime startTime;
    private LocalTime endTime;

}
