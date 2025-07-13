package com.bank.loan.loan_service.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDecisionDto {
    private String decision; // "APPROVED" or "REJECTED"
}