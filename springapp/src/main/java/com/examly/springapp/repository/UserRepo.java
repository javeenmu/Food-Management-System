package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.examly.springapp.model.User;

public interface UserRepo extends JpaRepository<User, Long>{

    @Query("select user from User user where user.email=?1")
    User findByEmail(String email);
    @Query("select user from User user where user.username=?1")
    User findByUsername(String username);

}
