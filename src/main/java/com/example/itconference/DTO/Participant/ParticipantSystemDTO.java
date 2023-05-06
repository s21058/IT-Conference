package com.example.itconference.DTO.Participant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
public class ParticipantSystemDTO {
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
}
