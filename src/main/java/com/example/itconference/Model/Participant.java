package com.example.itconference.Model;

import jakarta.persistence.*;

import javax.validation.constraints.Email;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
}
