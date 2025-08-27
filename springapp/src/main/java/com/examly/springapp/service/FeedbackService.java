package com.examly.springapp.service;

import java.util.List;
import java.util.Optional;

import com.examly.springapp.model.Feedback;

public interface FeedbackService {

    public Feedback createFeedback(Feedback feedback);

    public Optional<Feedback> getFeedbackById(long feedbackId);

    public List<Feedback> getAllFeedbacks();

    public boolean deleteFeedback(long feedbackId);

    public List<Feedback> getFeedbacksByUserId(long userId);

    public List<Feedback> getFeedbacksByFoodId(long foodId);

}
