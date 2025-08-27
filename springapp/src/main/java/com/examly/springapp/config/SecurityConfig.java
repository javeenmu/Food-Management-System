package com.examly.springapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(MyUserDetailsService myUserDetailsService, PasswordEncoder passwordEncoder,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain createFiSecurityFilterChain(HttpSecurity http) throws Exception {
        final String adminRole = "ADMIN";
        final String userRole = "USER";
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/register", "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user").hasAnyRole(adminRole)
                        .requestMatchers(HttpMethod.GET, "/api/food", "/api/orders/*", "/api/food/*", "/api/feedback/*")
                        .hasAnyRole(userRole, adminRole)
                        .requestMatchers(HttpMethod.GET, "/api/feedback", "/api/orders","/api/orders/all").hasAnyRole(adminRole)
                        .requestMatchers(HttpMethod.GET, "/api/orders/user/{userId}").hasAnyRole(userRole)
                        .requestMatchers(HttpMethod.GET, "/api/feedback/user/{userId}").hasAnyRole(userRole)
                        .requestMatchers(HttpMethod.POST, "/api/food").hasAnyRole(adminRole)
                        .requestMatchers(HttpMethod.POST, "/api/orders", "/api/feedback").hasAnyRole(userRole)
                        .requestMatchers(HttpMethod.PUT, "/api/food/{foodId}").hasAnyRole(adminRole)
                        .requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}").hasAnyRole(adminRole)
                        .requestMatchers(HttpMethod.DELETE, "/api/food/{foodId}").hasAnyRole(adminRole)
                        .requestMatchers(HttpMethod.DELETE, "/api/order/{orderId}", "/api/feedback/{feedbackId}")
                        .hasAnyRole(userRole)
                        .anyRequest().permitAll())
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic()
                .and()
                .build();
    }

    @Bean
    public AuthenticationManager createAuthenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder)
                .and().build();
    }

}
