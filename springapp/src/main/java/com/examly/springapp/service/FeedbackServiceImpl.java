package com.examly.springapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.examly.springapp.exceptions.FeedbackNotFoundException;
import com.examly.springapp.exceptions.FoodNotFoundException;
import com.examly.springapp.exceptions.InvalidDataException;
import com.examly.springapp.exceptions.UserNotFoundException;
import com.examly.springapp.model.Feedback;
import com.examly.springapp.repository.FeedbackRepo;

@Service
public class FeedbackServiceImpl implements FeedbackService {


    private final FeedbackRepo feedbackRepo;

    public FeedbackServiceImpl(FeedbackRepo feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    
    private static final Set<String> BAD_WORDS = Set.of("badword1", "badword2", "offensiveword");

    private boolean containsBadWords(String text) {
        String lowerText = text.toLowerCase();
        return BAD_WORDS.stream().anyMatch(lowerText::contains);
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        logger.info("Adding Feedback initialized");
        if (feedback.getRating() < 0 || feedback.getRating() > 5) {
            logger.error("Invalid Rating is given");
            throw new InvalidDataException("Invalid Rating");
        }
        if (containsBadWords(feedback.getFeedbackText())) {
           throw new InvalidDataException("Feedback rejected: contains inappropriate language.");
        }
        // if (feedback.getFood() == null) {
        //     logger.error("Food does not exists");
        //     throw new FoodNotFoundException("Food not found");
        // }
        // if (feedback.getUser() == null) {
        //     logger.error("User does not exists");
        //     throw new UserNotFoundException("User not found");
        // }
        feedback.setDate(LocalDate.now());
        feedback = feedbackRepo.save(feedback);
        logger.info("Feedback saved successfully");
        return feedback;
    }

    @Override
    public Optional<Feedback> getFeedbackById(long id) {
        logger.info("Getting feedback with ID: {} initialized", id);
        Feedback feedback = feedbackRepo.findById(id).orElse(null);
        if (feedback == null) {
            logger.error("Feedback does not exist");
            throw new FeedbackNotFoundException("Feedback does not exist.");
        }
        logger.info("Feedback with ID: {} is fetched", id);
        return Optional.of(feedback);
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        logger.info("Getting all feedback initialized");
        List<Feedback> feedbacks = feedbackRepo.findAll();
        if (feedbacks.isEmpty()) {
            logger.error("Feedback not found");
            throw new FeedbackNotFoundException("Feedbacks not found");
        }
        logger.info(("All the Feedbacks have been fetched"));
        return feedbacks;
    }

    @Override
    public boolean deleteFeedback(long id) {
        logger.info("Deleting Feedback initialized");
        Feedback existingFeedback = feedbackRepo.findById(id).orElse(null);
        if (existingFeedback == null) {
            logger.error("Feedback does not exist");
            throw new FeedbackNotFoundException("Feedback does not exist.");
        }
        feedbackRepo.deleteById(id);
        logger.info("Feedback deleted successfully");
        return true;
    }

    @Override
    public List<Feedback> getFeedbacksByUserId(long userId) {
        logger.info("Get All Feedbacks with user ID: {}, is initialized", userId);
        List<Feedback> feedbacks = feedbackRepo.getFeedBacksByUserId(userId);
        
        // if (feedbacks.isEmpty()) {
        //     logger.error("Feedback does not exists");
        //     throw new FeedbackNotFoundException("Feedback does not exist.");
        // }
        logger.info("All feedbacks with user ID: {} has been fetched successfully", userId);
        return feedbacks;
        
    }
    
    @Override
    public List<Feedback> getFeedbacksByFoodId(long foodId) {
        logger.info("Get All Feedbacks with food ID: {}, is initialized", foodId);
        List<Feedback> feedbacks = feedbackRepo.getFeedBacksByFoodId(foodId);
        if (!feedbacks.isEmpty()) {
            logger.error("Feedback does not exists");
            throw new FeedbackNotFoundException("Feedback does not exist.");
        }
        logger.info("All feedbacks with food ID: {} has been fetched successfully", foodId);
        return feedbacks;
    }

}