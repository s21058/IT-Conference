package com.example.itconference.Repository;

import com.example.itconference.Model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Lecture,Long> {
    List<Lecture>findByTopic(String topic);

    Lecture findById(Integer id);
}
