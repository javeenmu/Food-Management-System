package com.examly.springapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.examly.springapp.model.Orders;

@Repository
public interface OrderRepo extends JpaRepository<Orders, Integer> {

    @Query("select order from Orders order where order.user.userId=?1")
    Page<Orders> findOrdersByUserId(long userId,Pageable pageable);
    // List<Orders>findOrdersByUserId(long userId);
    @Query("select order from Orders order where order.food.foodId=?1")
    List<Orders> findOrderByFoodId(Object f);
    
    Page<Orders> findAll(Pageable pageable);

    }
