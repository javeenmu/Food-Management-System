package com.examly.springapp.service;

import java.util.List;

import com.examly.springapp.model.LoginDTO;
import com.examly.springapp.model.User;

import jakarta.validation.Valid;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers();

    LoginDTO loginUser(LoginDTO login);

    Object getUserByEmail(String email);

    User updateUserById(long userId, User user);

    User updateUserByEmail(long userId, User user);


}
