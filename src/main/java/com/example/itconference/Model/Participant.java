package com.example.itconference.Model;

import com.example.itconference.DTO.LectureDTO;
import com.example.itconference.DTO.Participant.ParticipantGetDTO;
import jakarta.persistence.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Entity
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
        ParticipantGetDTO getDTO = new ParticipantGetDTO();
        getDTO.setFirstName(participant.get().getFirstName());
        getDTO.setMiddleName(participant.get().getMiddleName());
        getDTO.setLastName(participant.get().getLastName());
        getDTO.setEmail(participant.get().getEmail());
        getDTO.setLogin(participant.get().getLogin());
        getDTO.setLectures(Lecture.parseToDTOList(participant.get().getLectures()));
        return getDTO;
    }

    public static ParticipantGetDTO toDTO(Participant participant) {
        ParticipantGetDTO getDTO = new ParticipantGetDTO();
        getDTO.setFirstName(participant.getFirstName());
        getDTO.setMiddleName(participant.getMiddleName());
        getDTO.setLastName(participant.getLastName());
        getDTO.setEmail(participant.getEmail());
        getDTO.setLogin(participant.getLogin());
        getDTO.setLectures(Lecture.parseToDTOList(participant.getLectures()));
        return getDTO;
    }
}
