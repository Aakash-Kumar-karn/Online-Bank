package com.bank.insurance.insurance_service.dto;

import com.bank.insurance.insurance_service.enums.InsuranceType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableInsuranceDto {

    @NotNull(message = "Insurance type is required")
    private InsuranceType type;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Premium must be provided")
    @Positive
    private Double premiumAmount;

    @NotNull(message = "Coverage must be provided")
    @Positive
    private Double coverageAmount;

    @NotNull(message = "Duration must be provided")
    @Min(1)
    private Integer durationInYears;
}
