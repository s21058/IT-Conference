package com.example.itconference.Service.ConferenceService;

import com.example.itconference.Model.Lecture;


import java.util.List;

public interface ConferenceService {
    List<Lecture>findAll();
    Lecture saveLecture(Lecture lecture);
    Lecture findById(Integer id);
    List<Lecture> findByTopic(String topic);

}
