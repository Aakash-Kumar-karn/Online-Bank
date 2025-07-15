package com.bank.loan.loan_service.controller;

import com.bank.loan.loan_service.dto.LoanDecisionDto;
import com.bank.loan.loan_service.dto.LoanDto;
import com.bank.loan.loan_service.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/test")
    public ResponseEntity<String> testCard() {

        return ResponseEntity.ok(" Loan Microservice is working!");
    }
    @GetMapping("/types")
    public ResponseEntity<List<String>> getLoanType(){
        List<String> list = loanService.getAllLoanTypes();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/apply")
    public ResponseEntity<LoanDto> applyLoan(@Valid @RequestBody LoanDto loanDto,
                                             @RequestHeader("X-USER-ID") Long userId){
        LoanDto result = loanService.applyForLoan(loanDto,userId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LoanDto>> getMyLoans(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        List<LoanDto> loans = loanService.getAllLoansByUser(userId);
        return new ResponseEntity<>(loans,HttpStatus.OK);
    }

    @PatchMapping("/{loanId}/decision")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanDto> decideLoanStatus(
            @PathVariable Long loanId,
            @RequestBody LoanDecisionDto dto
    ) {
        LoanDto updated = loanService.updateLoanStatus(loanId, dto);
        return new ResponseEntity<>(updated,HttpStatus.OK);
    }
    @PostMapping("/{loanId}/settle")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> settleLoan(
            @PathVariable Long loanId,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        loanService.settleLoan(loanId, userId);
        return new ResponseEntity<>("Loan settled successfully.",HttpStatus.OK);

    }

}
