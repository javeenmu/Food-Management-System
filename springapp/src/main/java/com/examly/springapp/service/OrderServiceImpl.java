package com.examly.springapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.examly.springapp.exceptions.FoodNotFoundException;
import com.examly.springapp.exceptions.InvalidDataException;
import com.examly.springapp.exceptions.OrderNotFoundException;
import com.examly.springapp.exceptions.UserNotFoundException;
import com.examly.springapp.model.Food;
import com.examly.springapp.model.Orders;
import com.examly.springapp.repository.FoodRepo;
import com.examly.springapp.repository.OrderRepo;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final FoodRepo foodRepo;

    public OrderServiceImpl(OrderRepo orderRepo, FoodRepo foodRepo) {
        this.orderRepo = orderRepo;
        this.foodRepo = foodRepo;
    }

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public Orders addOrder(Orders order) {
        logger.info("Adding Order, initialized");
        if (order.getFood() == null) {
            logger.error("Food for the Order with ID: {}, does not exist", order.getOrderId());
            throw new FoodNotFoundException("Food for the Order with ID " + order.getOrderId() + " does not exist");
        }
        if (order.getUser() == null) {
            logger.error("User for the Order with ID: {}, does not exist", order.getOrderId());
            throw new UserNotFoundException("User for the Order with ID " + order.getOrderId() + " does not exist");
        }
        if (order.getQuantity() < 0) {
            logger.error("Invalid Data");
            throw new InvalidDataException("Invalid Data");
        }
        Food food = foodRepo.findById(order.getFood().getFoodId()).orElse(null);

        // if (food == null) {
        //     int foodId = order.getFood().getFoodId();
        //     logger.error("Food with ID: {} does not exist", foodId);
        //     throw new FoodNotFoundException("Food with ID " + foodId + " does not exist");
        // }

        int updatedQuantity = food.getStockQuantity() - order.getQuantity();
        if (updatedQuantity < 0) {
            logger.error("User Quantity exceeds the existing quantity");
            throw new OrderNotFoundException("Sorry ,Order will not be placed it exceeds the existingQuantity"
                    + updatedQuantity + " " + food.getStockQuantity() + " " + order.getQuantity());
        }
        food.setStockQuantity(updatedQuantity);
        foodRepo.save(food);
        order.setFood(food);
        order.setOrderDate(LocalDate.now());

        logger.info("Order placed successfully");
        return orderRepo.save(order);
    }

    @Override
    public Optional<Orders> getOrderById(int orderId) {
        logger.info("Getting Order by ID: {}, initialized", orderId);
        Orders order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            logger.error("Order with ID: {}, not found", orderId);
            throw new OrderNotFoundException("Order with Id " + orderId + " is not found");
        }
        logger.info("Order with ID: {} fetched successfully", orderId);
        return orderRepo.findById(orderId);
    }

    // @Override
    // public List<Orders> getOrdersByUserId(long userId) {

    //     logger.info("Getting Order by user ID: {}, initialized", userId);
    //     List<Orders> orders = orderRepo.findOrdersByUserId(userId);
    //     if (orders.isEmpty()) {
    //         logger.error("Order with User ID: {}, is empty", userId);
    //         throw new OrderNotFoundException("Orders with the UserId " + userId + " is empty");
    //     }
    //     logger.info("Order with User ID: {} fetched successfully", userId);
    //     return orders;
    // }
    
    @Override
    public Page<Orders> getPaginatedOrdersByUserId(long userId,int page , int size) {
        Pageable pageable = PageRequest.of(page,size);
        return orderRepo.findOrdersByUserId(userId, pageable); 
    }

    @Override
    public List<Orders> getAllOrders() {
        logger.info("Getting All the orders has been initialized");
        List<Orders> orders = orderRepo.findAll();
        if (orders.isEmpty()) {
            logger.error("Order List is Empty");
            throw new OrderNotFoundException("Orders List is Empty");
        }
        logger.info("Order list fetched successfully");
        return orders;
    }

    @Override
    public Page<Orders> getAllPaginatedOrders(int page , int size) {
        Pageable pageable = PageRequest.of(page,size);
        return orderRepo.findAll(pageable); 
    }

    @Override
    public Orders updateOrder(Orders order, int orderId) {
        logger.info("Order update initiated");
        Orders existingOrder = orderRepo.findById(orderId).orElse(null);
        if (existingOrder == null) {
            logger.error("Order with ID {} is not found", orderId);
            throw new OrderNotFoundException("Order is not found");
        }
        if (order.getFood() == null) {
            logger.error("Food for the Order ID: {} does not exists", orderId);
            throw new FoodNotFoundException("Food for the Order does not exist");
        }
        if (order.getUser() == null) {

            logger.error("User for the Order ID: {} does not exists", orderId);
            throw new UserNotFoundException("User for the Order does not exist");
        }
        // if (order.getOrderStatus().equalsIgnoreCase("Cancelled") ||
        // order.getOrderStatus().equalsIgnoreCase("Completed")) {
        // logger.error("Update unsuccessful");
        // throw new InvalidDataException("Unable to update the order");
        // }
        int quantityChange = order.getQuantity() - existingOrder.getQuantity();
        Food food = existingOrder.getFood();
        if (quantityChange > 0) {
            if (food.getStockQuantity() > quantityChange) {
                logger.error("User Quantity exceeds the existing quantity");

            } else {
                logger.error("Quantity has been updated");
                food.setStockQuantity(food.getStockQuantity() - quantityChange);
            }
        } else {
            logger.error("Quantity has been updated");
            food.setStockQuantity(food.getStockQuantity() - quantityChange);
        }
        order.setOrderId(existingOrder.getOrderId());
        order.setOrderDate(LocalDate.now());
        order.setUser(existingOrder.getUser());
        foodRepo.save(food);
        order.setFood(food);
        logger.info("Order updated successfully");
        return orderRepo.save(order);
    }

    @Override
    public boolean deleteOrder(int orderId) {
        logger.info("Delete Order has been initiated");
        Orders existingOrder = orderRepo.findById(orderId).orElse(null);
        if (existingOrder == null) {
            logger.error("Order with ID: {} is not found", orderId);
            throw new OrderNotFoundException("Order with Id " + orderId + " is not found");
        }
        if (existingOrder.getOrderStatus().equals("On the way") || existingOrder.getOrderStatus().equals("Delivered")) {
            logger.error("Cancel is not possible");
            throw new InvalidDataException("Unable to cancel the order");
        }
        Food food = existingOrder.getFood();
        food.setStockQuantity(food.getStockQuantity() + existingOrder.getQuantity());
        foodRepo.save(food);
        existingOrder.setOrderStatus("Cancelled");
        orderRepo.save(existingOrder);
        logger.info("Order has been deleted successfully - status changed");
        return true;
    }
}
