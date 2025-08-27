package com.examly.springapp.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.config.JwtUtils;
import com.examly.springapp.model.Feedback;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepo;
import com.examly.springapp.service.FeedbackService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final JwtUtils jwtUtils;
    private final UserRepo userRepo;

    public FeedbackController(FeedbackService feedbackService, JwtUtils jwtUtils, UserRepo userRepo) {
        this.feedbackService = feedbackService;
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Feedback> createFeedback(@Valid @RequestBody Feedback feedback, HttpServletRequest request) {
        String email = jwtUtils.extractUsername(request.getHeader("Authorization").substring(7));
        User user = userRepo.findByEmail(email);
        feedback.setUser(user);
        feedback = feedbackService.createFeedback(feedback);
        return ResponseEntity.status(201).body(feedback);
    }

    @GetMapping("/{feedbackId}")
    @PreAuthorize("hasRole('USER' , 'ADMIN')")
    public ResponseEntity<Feedback> getFeedbackById(@Valid @PathVariable long feedbackId) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(feedbackId);
        return ResponseEntity.status(200).body(feedback.get());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.status(200).body(feedbacks);
    }

    @DeleteMapping("/{feedbackId}")
    @PreAuthorize("hasRole('USER','ADMIN')")
    public ResponseEntity<?> deleteFeedback(@Valid @PathVariable long feedbackId) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(feedbackId);
        boolean flag = feedbackService.deleteFeedback(feedbackId);
        if (flag) {
            return ResponseEntity.status(200).body(feedback.get());
        }
        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Feedback>> getFeedbacksByUserId(@Valid @PathVariable long userId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByUserId(userId);
        return ResponseEntity.status(200).body(feedbacks);
    }

    @GetMapping("/food/{foodId}")
    // @PreAuthorize("hasRole('USER','ADMIN')")
    public ResponseEntity<List<Feedback>> getFeedbacksByFoodId(@PathVariable long foodId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByFoodId(foodId);
        return ResponseEntity.status(200).body(feedbacks);
    }

}
