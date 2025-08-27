package com.examly.springapp.model;
 import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;

public class LoginDTO {

    @Email(message = "Enter Valid Email Address")
    private String email;
    @Nonnull
    private String password;
    private String token;

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}