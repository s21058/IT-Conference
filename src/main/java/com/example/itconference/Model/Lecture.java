package com.example.itconference.Model;

import com.example.itconference.DTO.LectureDTO;
import com.example.itconference.DTO.Participant.ParticipantGetDTO;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotNull
    private String topic;

    @Getter
    @Setter
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Getter
    @Setter
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Getter
    @Setter
    @Size(max = 5)
    @ManyToMany
    private List<Participant> participants;

    public Lecture() {

    }
    public static List<LectureDTO> parseToDTOList(List<Lecture> lectures) {
        return lectures.stream().map(lecture -> {
            LectureDTO dto = new LectureDTO();
            dto.setTopic(lecture.getTopic());
            dto.setStartTime(lecture.getStartTime());
            dto.setEndTime(lecture.getEndTime());
            return dto;
        }).collect(Collectors.toList());
    }
    public static LectureDTO parseToDTO(Lecture lecture){
        LectureDTO dto=new LectureDTO();
        dto.setTopic(lecture.getTopic());
        dto.setStartTime(lecture.getStartTime());
        dto.setEndTime(lecture.getEndTime());
        return dto;
    }

}
