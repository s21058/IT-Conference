package com.example.itconference.DTO.Participant;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

public class ParticipantReservationDTO {
    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    @Email(regexp = "^([\\w.\\-]+)@([\\w\\-]+)(\\.[\\w\\-]+)*([\\w\\-]+)\\.([a-zA-Z]{2,})$", message = "The email address is invalid")
    private String email;
}
