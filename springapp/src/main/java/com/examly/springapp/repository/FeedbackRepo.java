package com.examly.springapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.examly.springapp.model.Feedback;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, Long> {

    @Query("select feedback from Feedback feedback where feedback.food.foodId = ?1")
    List<Feedback> getFeedBacksByFoodId(long foodId);

    @Query("select feedback from Feedback feedback where feedback.user.userId = ?1")
    List<Feedback> getFeedBacksByUserId(long userId);

}