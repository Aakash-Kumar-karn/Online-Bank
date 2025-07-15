package com.bank.loan.loan_service.service.impl;

import com.bank.loan.loan_service.dto.LoanDecisionDto;
import com.bank.loan.loan_service.dto.LoanDto;
import com.bank.loan.loan_service.dto.NotificationRequestDto;
import com.bank.loan.loan_service.enums.LoanStatus;
import com.bank.loan.loan_service.enums.LoanType;
import com.bank.loan.loan_service.feign.NotificationClient;
import com.bank.loan.loan_service.feign.TransactionClient;
import com.bank.loan.loan_service.feign.UserClient;
import com.bank.loan.loan_service.model.Loan;
import com.bank.loan.loan_service.repository.LoanRepository;
import com.bank.loan.loan_service.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final UserClient userClient;
    private final LoanRepository loanRepository;
    private final NotificationClient notificationClient;
    private final TransactionClient  transactionClient;
    @Override
    public List<String> getAllLoanTypes() {
        //return loanRepository.findAll();
        return Arrays.stream(LoanType.values())//LoanType.values()	Show available loan types
                .map(Enum::name)               //loanRepository.findAll()	Fetch issued loans from DB
                .collect(Collectors.toList());
    }

    @Override
    public LoanDto applyForLoan(LoanDto loanDto, Long userId) {

        //validate before go on
        boolean exists = userClient.checkUserExists(userId);
        if(!exists){
            throw new IllegalArgumentException("User does not exist !");
        }

        Loan loan = new Loan();
        loan.setUserId(userId);
        loan.setAmount(loanDto.getAmount());
        loan.setInterestRate(loanDto.getInterestRate());
        loan.setLoanType(loanDto.getLoanType());
        loan.setDurationInMonths(loanDto.getDurationInMonths());

        loan.setStatus(LoanStatus.PENDING);
        loan.setIssuedAt(LocalDateTime.now());

        Loan saved = loanRepository.save(loan);

        LoanDto response = new LoanDto();
        response.setId(saved.getId());
        response.setLoanType(saved.getLoanType());
        response.setAmount(saved.getAmount());
        response.setInterestRate(saved.getInterestRate());
        response.setDurationInMonths(saved.getDurationInMonths());
        response.setStatus(saved.getStatus());
        return response;
    }
    @Override
    public List<LoanDto> getAllLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    private LoanDto convertToDto(Loan loan) {
        return new LoanDto(
                loan.getId(),
                loan.getLoanType(),
                loan.getAmount(),
                loan.getDurationInMonths(),
                loan.getInterestRate(),
                loan.getStatus()
        );
    }
    @Override
    public LoanDto updateLoanStatus(Long loanId, LoanDecisionDto dto) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        // Validate input
        if (!dto.getDecision().equalsIgnoreCase("APPROVED") && ! dto.getDecision().equalsIgnoreCase("REJECTED")) {
            throw new IllegalArgumentException("Invalid loan decision. Use APPROVED or REJECTED.");
        }


        loan.setStatus(LoanStatus.valueOf(dto.getDecision().toUpperCase()));
        loanRepository.save(loan);

        //  Send notification
        NotificationRequestDto notif = new NotificationRequestDto();
        notif.setUserId(loan.getUserId());
        notif.setTitle("Loan Status Update");
        notif.setMsg("Your loan has been " + dto.getDecision().toUpperCase());
        notif.setType("email");

        try {
            notificationClient.sendNotification(notif);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }

        return convertToDto(loan);
    }

    @Override
    public void settleLoan(Long loanId, Long userId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (!loan.getUserId().equals(userId)) {
            throw new AccessDeniedException("You can only settle your own loans");
        }

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new IllegalStateException("Only approved loans can be settled");
        }

        //  Step 1: Deduct from account via TransactionMS
        transactionClient.settleLoan(userId, loan.getAmount());


        //  Step 2: Mark as SETTLED
        loan.setStatus(LoanStatus.SETTLED);
        loanRepository.save(loan);

        //  Step 3: Send notification

        NotificationRequestDto notif = new NotificationRequestDto();
        notif.setUserId(userId);
        notif.setTitle("Loan Settled");
        notif.setMsg("Your loan of amount ₹" + loan.getAmount() + " has been settled.");
        notif.setType("Email");

        try {
            notificationClient.sendNotification(notif);
        } catch (Exception e) {
            System.err.println(" Failed to send notification: " + e.getMessage());
        }
    }


}