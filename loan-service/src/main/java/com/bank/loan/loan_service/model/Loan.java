package com.bank.loan.loan_service.model;


import com.bank.loan.loan_service.enums.LoanStatus;
import com.bank.loan.loan_service.enums.LoanType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    private Double amount;

    private Integer durationInMonths;

    private Double interestRate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.PENDING;

    private LocalDateTime issuedAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
