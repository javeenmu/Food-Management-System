package com.examly.springapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.examly.springapp.exceptions.FoodNotFoundException;
import com.examly.springapp.exceptions.InvalidDataException;
import com.examly.springapp.exceptions.UserNotFoundException;
import com.examly.springapp.model.LoginDTO;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public User createUser(User user) {
        logger.info("Attempting to add user");
        if (userRepo.findByEmail(user.getEmail()) != null) {
            logger.error("Email ID already exist!");
            throw new InvalidDataException("Email ID already associated with another account");
        }
        if (!(user.getMobileNumber().length() == 10
                && (user.getUserRole().toString().equals("ADMIN") || user.getUserRole().toString().equals("USER")))) {
            logger.error("Invalid Data");
            throw new InvalidDataException("Invalid Data");

        }
        user.setPassword(encoder.encode(user.getPassword()));
        logger.info("User has been added");
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Attempting to fetch all Users");
        return userRepo.findAll();
    }

    @Override
    public LoginDTO loginUser(LoginDTO login) {

        logger.debug("Attempting to Login");
        User existingUser = userRepo.findByEmail(login.getEmail());
        if (existingUser == null) {
            logger.error("Email do not exist, Unsuccessful");
            throw new FoodNotFoundException("Login unSuccessful");

        }
        if (encoder.matches(login.getPassword(), existingUser.getPassword())) {
            login.setPassword(existingUser.getPassword());
            return login;
        }

        logger.error("Wrong Password, Login Unsuccessful");
        throw new InvalidDataException("Login unSuccessful");

    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);

    }

    @Override
    public User updateUserById(long userId, User user) {
        User existingUser = userRepo.findById(userId).orElse(null);
        if (existingUser == null) {
            throw new UserNotFoundException("User with the id " + userId + " not exists");
        }
        if (!user.getEmail().equals(existingUser.getEmail()) && userRepo.findByEmail(user.getEmail()) != null) {
            logger.error("Email ID already exist!");
            throw new InvalidDataException("Email ID already associated with another account");
        }
        if (!user.getUsername().equals((existingUser.getUsername()))
                && userRepo.findByUsername(user.getUsername()) != null) {
            logger.error("Username already exist!");
            throw new InvalidDataException("Username already associated with another account");
        }

        // existingUser.setEmail(user.getEmail());
        // existingUser.setUsername(user.getUsername());
        // existingUser.setUserId(userId);
        // existingUser.setMobileNumber(user.getMobileNumber());
        user.setUserId(existingUser.getUserId());
        return userRepo.save(user);
    }

    @Override
    public User updateUserByEmail(long userId, User user) {
        User existingUser = userRepo.findById(userId).orElse(null);
        if (existingUser == null) {
            throw new UserNotFoundException("User not exists");
        }
        System.out.println(existingUser.getPassword());
        System.out.println(user.getPassword());
        existingUser.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(existingUser);
    }

}
