package com.examly.springapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.config.JwtUtils;
import com.examly.springapp.config.MyUserDetailsService;
import com.examly.springapp.model.LoginDTO;
import com.examly.springapp.model.User;
import com.examly.springapp.service.UserService;

import jakarta.validation.Valid;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
            MyUserDetailsService myUserDetailsService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/api/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        user = userService.createUser(user);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping("/api/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDTO login) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(login.getEmail());
        String jwtToken = jwtUtils.createToken(userDetails);
        login.setToken(jwtToken);
        login = userService.loginUser(login);
        return ResponseEntity.status(200).body(login);
    }

    @GetMapping("/api/email/{email}")
    public ResponseEntity<?> getUser(@Valid @PathVariable String email) {
        return ResponseEntity.status(200).body(userService.getUserByEmail(email));
    }
    @PutMapping("/api/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserById(@Valid @PathVariable long userId,@RequestBody User user) {
        user = userService.updateUserById(userId,user);
        return ResponseEntity.status(200).body(user);
    }

    @PutMapping("/api/user/password/{userId}")
    public ResponseEntity<?> updateUserByEmail(@PathVariable long userId, @RequestBody User user){
        user = userService.updateUserByEmail(userId, user);
        return ResponseEntity.status(200).body(user);

    }
}
