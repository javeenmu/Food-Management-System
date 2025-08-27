package com.examly.springapp.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.examly.springapp.exceptions.UserNotFoundException;

import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("No User Exists with the Email " + email);
        }
        return UserPrinciple.build(user);
    }

}
