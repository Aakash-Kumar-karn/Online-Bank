package com.bank.insurance.insurance_service.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInsuranceResponseDto {
    private String planName;
    private String description;
    private Double amount;
    private Integer durationInYears;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;
}
