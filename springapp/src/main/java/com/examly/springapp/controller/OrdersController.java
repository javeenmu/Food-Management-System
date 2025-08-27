package com.examly.springapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.config.JwtUtils;
import com.examly.springapp.model.Orders;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepo;
import com.examly.springapp.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    private final OrderService orderService;
    private final JwtUtils jwtUtils;
    private final UserRepo userRepo;

    public OrdersController(OrderService orderService, JwtUtils jwtUtils, UserRepo userRepo) {
        this.orderService = orderService;
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Orders> addOrder(@Valid @RequestBody Orders order, HttpServletRequest request) {
        String email = jwtUtils.extractUsername(request.getHeader("Authorization").substring(7));
        User user = userRepo.findByEmail(email);
        order.setUser(user);
        Orders createdOrder = orderService.addOrder(order);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN','USER')")
    public ResponseEntity<Orders> getOrderById(@Valid @PathVariable int orderId) {
        Optional<Orders> order = orderService.getOrderById(orderId);
        return ResponseEntity.status(200).body(order.get());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    // public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable long userId) {
    //     List<Orders> orders = orderService.getOrdersByUserId(userId);
    //     return ResponseEntity.status(200).body(orders);
    // }
    public ResponseEntity<Page<Orders>> getPaginatedOrdersByUserId(@Valid @PathVariable long userId,@RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "2") int size) {
        Page<Orders> orders = orderService.getPaginatedOrdersByUserId(userId,page,size);
        return ResponseEntity.status(200).body(orders);
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Orders>> getAllOrders() {
        List<Orders> orders = orderService.getAllOrders();
        return ResponseEntity.status(200).body(orders);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<?> getAllOrders() {
    //     List<Orders> orders = orderService.getAllOrders();
    //     return ResponseEntity.status(200).body(orders);
    // }
    public ResponseEntity<Page<Orders>> getAllOrders(@RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "2") int size) {
        Page<Orders> orders = orderService.getAllPaginatedOrders(page , size);
        return ResponseEntity.status(200).body(orders);
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody Orders order, @PathVariable int orderId) {
        Orders updatedOrder = orderService.updateOrder(order, orderId);
        return ResponseEntity.status(200).body(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Orders> deleteOrder(@Valid @PathVariable int orderId) {
        Optional<Orders> orderToBeDeleted = orderService.getOrderById(orderId);
        orderService.deleteOrder(orderId);
        return ResponseEntity.status(200).body(orderToBeDeleted.get());
    }
}