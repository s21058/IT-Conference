package com.example.itconference.Repository;

import com.example.itconference.Model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    Optional<Participant> findByLogin(String login);

    Optional<Participant> findByEmail(String email);

    Participant findById(Integer id);
    boolean existsByEmail(String email);

    int countByEmail(String email);

    boolean existsByLogin(String login);
}
