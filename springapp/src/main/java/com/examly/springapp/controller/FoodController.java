package com.examly.springapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.config.JwtUtils;
import com.examly.springapp.model.Food;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepo;
import com.examly.springapp.service.FoodService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    private final FoodService foodService;
    private final JwtUtils jwtUtils;
    private final UserRepo userRepo;

    public FoodController(FoodService foodService, JwtUtils jwtUtils, UserRepo userRepo) {
        this.foodService = foodService;
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Food> addFood(@Valid @RequestBody Food food, HttpServletRequest request) {
        String email = jwtUtils.extractUsername(request.getHeader("Authorization").substring(7));
        User user = userRepo.findByEmail(email);
        food.setUser(user);
        food = foodService.addFood(food);
        return ResponseEntity.status(201).body(food);
    }

    @GetMapping("/{foodId}")
    @PreAuthorize("hasRole('ADMIN','USER')")
    public ResponseEntity<Food> getFoodById(@Valid @PathVariable int foodId) {
        Optional<Food> food = foodService.getFoodById(foodId);
        return ResponseEntity.status(200).body(food.get());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN','USER')")
    // public ResponseEntity<?> getAllFoods() {
    //     List<Food> foods = foodService.getAllFoods();
    //     return ResponseEntity.status(200).body(foods);
    // }

    public ResponseEntity<Page<Food>> getFoods(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "2") int size) {

    Page<Food> foodPage = foodService.getPaginatedFoods(page, size);

    // Map<String, Object> response = new HashMap<>();
    // response.put("foods", foodPage.getContent());
    // response.put("currentPage", foodPage.getNumber());
    // response.put("totalItems", foodPage.getTotalElements());
    // response.put("totalPages", foodPage.getTotalPages());

    return ResponseEntity.status(200).body(foodPage);
}


    @PutMapping("/{foodId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Food> updateFood(@Valid @PathVariable int foodId, @RequestBody Food food) {
        food = foodService.updateFood(foodId, food);
        return ResponseEntity.status(200).body(food);
    }

    @DeleteMapping("/{foodId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteFood(@Valid @PathVariable int foodId) {
        boolean flag = foodService.deleteFood(foodId);
        if (flag) {
            return ResponseEntity.status(200).body(flag);
        } else {
            return ResponseEntity.status(404).body(flag);
        }
    }

    @GetMapping("/user/{userId}")
    // @PreAuthorize("hasRole('ADMIN','USER')")
    public ResponseEntity<List<Food>> getFoodsByUserId(@Valid @PathVariable long userId) {
        List<Food> foods = foodService.getFoodsByUserId(userId);
        return ResponseEntity.status(200).body(foods);
    }

}