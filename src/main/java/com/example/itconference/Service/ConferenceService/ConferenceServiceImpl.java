package com.example.itconference.Service.ConferenceService;

import com.example.itconference.Model.Lecture;
import com.example.itconference.Repository.ConferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConferenceServiceImpl implements ConferenceService {

    final
    ConferenceRepository conferenceRepository;

    public ConferenceServiceImpl(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    @Override
    public List<Lecture> findAll() {
        return conferenceRepository.findAll();
    }

    @Override
    public Lecture saveLecture(Lecture lecture) {
        return conferenceRepository.save(lecture);
    }

    @Override
    public Lecture findById(Integer id) {
        return conferenceRepository.findById(id);
    }

    @Override
    public List<Lecture> findByTopic(String topic) {
        return conferenceRepository.findByTopic(topic);
    }

}
