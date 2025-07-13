package com.bank.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 10, message = "Mobile number must be exactly 10 digits")
    private String mobile;
}

// Is it necessary to put password in UserDto?
// Yes — in the input DTO used for Register/Login, it’s necessary.
//Why?
//
// Because the password must come from the client when:
//Registering a new user (so you can save it)
//
//Logging in (so you can compare it)
//Frontend → sends UserDto (includes raw password)
//Backend → hashes it → stores in DB (Register)
//OR
//Backend → matches hash (Login)
