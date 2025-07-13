package com.bank.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token; // JWT token
    private String message;// e.g. "Login successful"
    private UserDto user;
}
