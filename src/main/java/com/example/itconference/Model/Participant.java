package com.example.itconference.Model;

import com.example.itconference.DTO.Participant.ParticipantGetDTO;
import com.example.itconference.DTO.Participant.ParticipantInLectureDTO;
import com.example.itconference.DTO.Participant.ParticipantReservationDTO;
import jakarta.persistence.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
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

    public Participant() {
    }

    public static ParticipantGetDTO toDTO(Optional<Participant> participant) {
        return new ParticipantGetDTO(
                participant.get().getFirstName(),
                participant.get().getMiddleName(),
                participant.get().getLastName(),
                participant.get().getLogin(),
                participant.get().getEmail(),
                Lecture.parseToDTOList(participant.get().getLectures()));
    }

    public static ParticipantGetDTO toDTO(Participant participant) {
        return new ParticipantGetDTO(
                participant.getFirstName(),
                participant.getMiddleName(),
                participant.getLastName(),
                participant.getLogin(),
                participant.getEmail(),
                Lecture.parseToDTOList(participant.getLectures()));
    }
    public static List<ParticipantInLectureDTO> toLectureDTO(List<Participant> participant) {
       return participant.stream().map(l->new ParticipantInLectureDTO(
               l.getFirstName(),
               l.getMiddleName(),
               l.getLastName(),
               l.getLogin(),
               l.getEmail()
       )).collect(Collectors.toList());
    }
    public static List<ParticipantGetDTO> toDTO(List<Participant> participant) {
       return participant.stream().map(Participant::toDTO).collect(Collectors.toList());
    }

}
