package com.example.pizza_world.service;

import com.example.pizza_world.bean.Feedback;
import com.example.pizza_world.dao.FeedbackDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackDao feedbackDao;

    public void deleteFeedback(int feedbackId) {
        Feedback feedback = feedbackDao.findById(feedbackId, Feedback.class);
        feedbackDao.delete(feedback);
    }
}
