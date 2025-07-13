package com.bank.loan.loan_service.dto;

import com.bank.loan.loan_service.enums.LoanStatus;
import com.bank.loan.loan_service.enums.LoanType;
import jakarta.validation.constraints.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDto {

    private Long id;

    @NotNull(message = "Loan type is required")
    private LoanType loanType;

    @NotNull(message = "Amount is required")
    @Min(value = 1000, message = "Minimum loan amount should be 1000")
    private Double amount;

    @NotNull(message = "Duration is required")
    @Min(value = 6, message = "Minimum duration is 6 months")
    private Integer durationInMonths;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "1.0", message = "Minimum interest rate is 1%")
    private Double interestRate;

    private LoanStatus status;
}
