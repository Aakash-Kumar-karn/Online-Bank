package com.bank.card.CardService.config;

import com.bank.card.CardService.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) //  Disable CSRF for APIs
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //  No session — stateless auth
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/cards/is-valid/**", "/api/cards/type/**").permitAll()
                        //  Admin-only routes
                        .requestMatchers("/api/cards/upgrade-requests/**").hasRole("ADMIN")
                        //  User-only routes
                        .requestMatchers("/api/cards/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) //  Add our custom JWT filter
                .build();
    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
//            throws Exception {
//        return config.getAuthenticationManager();
//    }
}

// Code Flow
// Every request sends Authorization: Bearer <token> in header
//Our JwtAuthFilter intercepts the request
//Token is parsed → role is extracted
// SecurityContextHolder is populated with ROLE_USER or ROLE_ADMIN
// Spring Security checks @PreAuthorize(...) on the controller method
// Access granted or  403 returned
