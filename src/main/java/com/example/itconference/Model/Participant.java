package com.example.itconference.Model;

import com.example.itconference.DTO.Participant.ParticipantGetDTO;
import com.example.itconference.DTO.Participant.ParticipantInLectureDTO;
import com.example.itconference.DTO.Participant.ParticipantSystemDTO;
import jakarta.persistence.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Participant {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Getter
    @Setter
    @NotNull
    private String login;

    @Getter
    @Setter
    @NotNull
    @Size(min = 2, max = 30, message = "First Name seems too be short or too long")
    private String firstName;

    @Getter
    @Setter
    @Nullable
    @Size(max = 30, message = "Middle name seems to be more than 30 characters")
    private String middleName;

    @Getter
    @Setter
    @NotNull
    @Size(min = 2, max = 30, message = "Last Name seems too be short or too long")
    private String lastName;

    @Getter
    @Setter
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9._%+-]{2,}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "The email address is invalid")
    private String email;

    @Getter
    @Setter
    @ManyToMany
    private List<Lecture> lectures;


    public static ParticipantGetDTO toDTO(Participant participant) {
        return new ParticipantGetDTO(
                participant.getFirstName(),
                participant.getMiddleName(),
                participant.getLastName(),
                participant.getLogin(),
                participant.getEmail(),
                Lecture.parseToDTOList(participant.getLectures()));
    }
    public static List<ParticipantInLectureDTO> toDTOinLecture(List<Participant> participant) {
       return participant.stream().map(l->new ParticipantInLectureDTO(
               l.getFirstName(),
               l.getMiddleName(),
               l.getLastName(),
               l.getLogin(),
               l.getEmail()
       )).collect(Collectors.toList());
    }
    public static List<ParticipantSystemDTO> toDTOinSystem(List<Participant> participant) {
        return participant.stream().map(l->new ParticipantSystemDTO(
                l.getFirstName(),
                l.getMiddleName(),
                l.getLastName(),
                l.getEmail()
        )).collect(Collectors.toList());
    }

    public void addLecture(Lecture lecture){
        lectures.add(lecture);
    }
    public void deleteLecture(Lecture lecture){
        lectures.remove(lecture);
    }

}
