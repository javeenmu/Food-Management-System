package com.examly.springapp.service;

import java.util.Optional;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.examly.springapp.exceptions.FoodNotFoundException;
import com.examly.springapp.exceptions.InvalidDataException;
import com.examly.springapp.exceptions.UserNotFoundException;
import com.examly.springapp.model.Food;
import com.examly.springapp.model.Orders;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.FoodRepo;
import com.examly.springapp.repository.OrderRepo;
import com.examly.springapp.repository.UserRepo;

@Service
public class FoodServiceImpl implements FoodService {
    private final FoodRepo foodRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private static final Logger logger = LoggerFactory.getLogger(FoodServiceImpl.class);

    public FoodServiceImpl(FoodRepo foodRepo, UserRepo userRepo , OrderRepo orderRepo) {
        this.foodRepo = foodRepo;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    public Food addFood(Food food) {
        logger.info("Adding food initialized");
        if (food.getPrice() < 0 || food.getStockQuantity() < 0) {
            logger.error("Invalid Data");
            throw new InvalidDataException("Invalid Data");
        }
        if (foodRepo.getFoodByName(food.getFoodName()) != null) {
            logger.error("Food with this name: {},  already exists", food.getFoodName());
            throw new InvalidDataException("Food with name already exists");
        }
        return foodRepo.save(food);
    }

    @Override
    public Optional<Food> getFoodById(int foodId) {
        logger.info("Getting food by ID: {}, initialized", foodId);
        Optional<Food> food = foodRepo.findById(foodId);
        if (food.isEmpty()) {
            logger.error("Food Item not found");
            throw new FoodNotFoundException("Food Item not found");
        }
        logger.info("Fetching the food with ID: {}", foodId);
        return food;
    }
    
    // @Override
    // public List<Food> getAllFoods() {
        
    //     logger.info("Getting all foods, initialized");
    //     List<Food> foods = foodRepo.findAll();
    //     if (foods.isEmpty()) {
    //         logger.error("No food items are there");
    //         throw new FoodNotFoundException("No food items are available");
    //     }
    //     logger.info("Foods are displayed");
    //     return foods;
    // }
    @Override
    public Page<Food> getPaginatedFoods(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return foodRepo.findAll(pageable);
    }


    @Override
    public Food updateFood(int foodId, Food food) {
        logger.info("Updating food by ID: {}, initialized", foodId);
        Food existingFood = foodRepo.findById(foodId).orElse(null);
        if (existingFood == null) {
            logger.error("Foot item is not there in the list");
            throw new FoodNotFoundException("Food Item not available");
        }
        if (food.getPrice() < 0 || food.getPrice() < 0) {
            logger.error("Invalid Data");
            throw new InvalidDataException("Invalid Data");
        }
        // if (foodRepo.getFoodByName(food.getFoodName()) != null) {
        //     logger.error("Food with name already exists");
        //     throw new InvalidDataException("Food with name already exists");
        // }
        food.setFoodId(existingFood.getFoodId());
        food.setUser(existingFood.getUser());
        logger.info("Food updated Successfully");
        return foodRepo.save(food);
    }

    @Override
    public boolean deleteFood(int foodId) {
        boolean flag=false;
        logger.info("Deleting food by ID: {}, initialized", foodId);
        Food existingFood = foodRepo.findById(foodId).orElse(null);
        if (existingFood == null) {
            logger.error("Food Item not available");
            throw new FoodNotFoundException("Food Item not available");
        }
        List<Orders> orders=orderRepo.findOrderByFoodId(foodId);
        if(orders.isEmpty()){foodRepo.deleteById(foodId);}
        else{existingFood.setStockQuantity(0);foodRepo.save(existingFood);}
        
        logger.info("Food deleted successfully");
        return true;
    }
    
    @Override
    public List<Food> getFoodsByUserId(long userId) {
        logger.info("Getting foods by user ID: {} has been initialized", userId);
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            logger.error("User is not there");
            throw new UserNotFoundException("User not found");
        }
        List<Food> foods = foodRepo.getFoodsByUserId(userId);
        if (foods.isEmpty()) {
            logger.error("No Food items are there");
            throw new FoodNotFoundException("No Foods items available");
        }
        logger.info("Food with User ID: {}, fetched", userId);
        return foods;
    }

}
