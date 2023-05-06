package com.example.itconference.Model;

import com.example.itconference.DTO.Lecture.LectureDTO;
import com.example.itconference.DTO.Lecture.LectureGetDTO;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
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
    public static List<LectureGetDTO> parseToDTOList(List<Lecture> lectures) {
        return lectures.stream().map(lecture -> new LectureGetDTO(
                lecture.getTopic(),
                lecture.getStartTime(),
                lecture.getEndTime()
        )).collect(Collectors.toList());
    }
    public static List<LectureDTO> parseToDTO(List<Lecture> lectures) {
        return lectures.stream().map(lecture -> new LectureDTO(
                lecture.getTopic(),
                lecture.getStartTime(),
                lecture.getEndTime(),
                Participant.toDTOinLecture(lecture.getParticipants())
        )).collect(Collectors.toList());
    }
    public static LectureDTO parseToDTO(Lecture lecture){
        LectureDTO dto=new LectureDTO();
        dto.setTopic(lecture.getTopic());
        dto.setStartTime(lecture.getStartTime());
        dto.setEndTime(lecture.getEndTime());
        dto.setParticipants(Participant.toDTOinLecture(lecture.getParticipants()));
        return dto;
    }
    public void addParticipant(Participant participant){
        participants.add(participant);
    }
}
