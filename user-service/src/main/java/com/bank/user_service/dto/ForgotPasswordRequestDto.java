package com.bank.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ForgotPasswordRequestDto {

    @Email
    @NotBlank
    private String email;
}
