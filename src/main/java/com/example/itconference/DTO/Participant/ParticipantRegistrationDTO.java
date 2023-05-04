package com.example.itconference.DTO.Participant;

import com.example.itconference.Model.Participant;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ParticipantRegistrationDTO {
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
    @Email(regexp = "^([\\w.\\-]+)@([\\w\\-]+)(\\.[\\w\\-]+)*([\\w\\-]+)\\.([a-zA-Z]{2,})$", message = "The email address is invalid")
    private String email;


    public Participant toParticipant(){
        Participant participant=new Participant();
        participant.setFirstName(this.getFirstName());
        participant.setLastName(this.getLastName());
        participant.setMiddleName(this.getMiddleName());
        participant.setLogin(this.getLogin());
        participant.setEmail(this.getEmail());
        return participant;
    }
}
