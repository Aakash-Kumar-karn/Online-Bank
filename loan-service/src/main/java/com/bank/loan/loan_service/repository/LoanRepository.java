package com.bank.loan.loan_service.repository;

import com.bank.loan.loan_service.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Long> {

    List<Loan> findByUserId(Long userId);
}
