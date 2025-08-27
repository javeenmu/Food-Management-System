package com.examly.springapp.service;

import com.examly.springapp.model.Food;
import java.util.Optional;

import org.springframework.data.domain.Page;

import java.util.List;

public interface FoodService {

    Food addFood(Food food);

    Optional<Food> getFoodById(int foodId);

    // List<Food> getAllFoods();
    Page<Food> getPaginatedFoods(int page, int size);

    Food updateFood(int foodId, Food food);

    boolean deleteFood(int foodId);

    List<Food> getFoodsByUserId(long userId);

}
