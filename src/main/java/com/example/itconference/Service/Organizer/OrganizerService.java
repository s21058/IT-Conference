package com.example.itconference.Service.Organizer;

import org.springframework.http.ResponseEntity;

public interface OrganizerService {
    ResponseEntity<?> interestInLecture(Integer id);
}
