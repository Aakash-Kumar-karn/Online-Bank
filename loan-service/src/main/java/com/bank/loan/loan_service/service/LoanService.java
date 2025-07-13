package com.bank.loan.loan_service.service;

import com.bank.loan.loan_service.dto.LoanDecisionDto;
import com.bank.loan.loan_service.dto.LoanDto;

import java.util.List;

public interface LoanService {
   List<String> getAllLoanTypes();
   LoanDto applyForLoan(LoanDto loanDto, Long userId);

   List<LoanDto> getAllLoansByUser(Long userId);

   LoanDto updateLoanStatus(Long loanId , LoanDecisionDto dto);
   void settleLoan(Long loanId, Long userId);

}
