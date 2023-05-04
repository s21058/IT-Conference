package com.example.itconference.CustomAnnotation.Login;

import com.example.itconference.Repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin,String> {
    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (login == null) {
            return true; // null values are allowed
        }
        return participantRepository.findByLogin(login) == null;
    }
}
