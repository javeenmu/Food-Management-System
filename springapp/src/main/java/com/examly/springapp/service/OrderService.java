package com.examly.springapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.examly.springapp.model.Orders;

public interface OrderService {

    Orders addOrder(Orders order);

    Optional<Orders> getOrderById(int orderId);

    // List<Orders> getOrdersByUserId(long userId);
    Page<Orders> getPaginatedOrdersByUserId(long userId , int page , int size);

    List<Orders> getAllOrders();
    Page<Orders> getAllPaginatedOrders(int page , int size);

    Orders updateOrder(Orders order, int orderId);

    boolean deleteOrder(int orderId);

}
