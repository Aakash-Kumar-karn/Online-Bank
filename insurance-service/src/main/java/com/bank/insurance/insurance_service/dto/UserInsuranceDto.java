package com.bank.insurance.insurance_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInsuranceDto {

    @NotNull(message = "Insurance ID is required")
    private Long insuranceId;

    @NotNull(message = "User ID is required")
    private Long userId;
}
