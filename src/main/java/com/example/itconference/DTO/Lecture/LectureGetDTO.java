package com.example.itconference.DTO.Lecture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
@NoArgsConstructor
@AllArgsConstructor
public class LectureGetDTO {
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
