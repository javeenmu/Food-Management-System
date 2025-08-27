package com.examly.springapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.examly.springapp.model.Food;

@Repository
public interface FoodRepo extends JpaRepository<Food, Integer> {

    @Query("select food from Food food where food.user.userId = ?1")
    List<Food> getFoodsByUserId(long userId);

    @Query("select food from Food food where food.foodName=?1")
    Food getFoodByName(String foodName);

    Page<Food> findAll(Pageable pageable);

}
