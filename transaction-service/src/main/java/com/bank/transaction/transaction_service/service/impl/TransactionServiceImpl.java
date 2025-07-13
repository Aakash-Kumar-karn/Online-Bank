package com.bank.transaction.transaction_service.service.impl;

import com.bank.transaction.transaction_service.dto.NotificationRequestDto;
import com.bank.transaction.transaction_service.dto.TransactionRequestDto;
import com.bank.transaction.transaction_service.dto.TransactionResponseDto;
import com.bank.transaction.transaction_service.exception.CardAlreadyBlockedException;
import com.bank.transaction.transaction_service.exception.InsufficientBalanceException;
import com.bank.transaction.transaction_service.feign.CardClient;
import com.bank.transaction.transaction_service.feign.NotificationClient;
import com.bank.transaction.transaction_service.feign.UserClient;
import com.bank.transaction.transaction_service.model.Transaction;
import com.bank.transaction.transaction_service.repository.TransactionRepository;
import com.bank.transaction.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepo;

    private final NotificationClient notificationClient;

    private final UserClient userClient;

    private final CardClient cardClient;

    public Double getLastBalance(Long userId){
        return transactionRepo.findByUserIdOrderByTimestampDesc(userId)
                .stream().findFirst()
                .map(transaction->transaction.getBalanceAfterTransaction())
                .orElse(0.0); // default balance if no history
    }

    @Override
    public TransactionResponseDto deposit(TransactionRequestDto dto, Long userId) {

        Double currentBal = getLastBalance(userId);
        Double newBal = currentBal + dto.getAmount();

        Transaction Tx = new Transaction();

       // Tx.setId(null); //auto-generated
        Tx.setUserId(userId);

        Tx.setAmount(dto.getAmount());
        Tx.setDescription(dto.getDescription());
        Tx.setType(Transaction.TransactionType.DEPOSIT);

        Tx.setTimestamp(LocalDateTime.now());
        Tx.setBalanceAfterTransaction(newBal);

        Transaction savedTransaction = transactionRepo.save(Tx);

        //send notification via feign

        NotificationRequestDto notification = new NotificationRequestDto();
        notification.setUserId(userId);
        notification.setTitle("Deposit Successful");
        notification.setMsg("You deposited Rs" + dto.getAmount() + ". Available balance: Rs" + newBal);
        notification.setType("EMAIL");

        try {
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Notification failed: " + e.getMessage());
        }
        return new TransactionResponseDto(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getDescription(),
                savedTransaction.getTimestamp(),
                savedTransaction.getBalanceAfterTransaction()
        );
    }

    @Override
    public Double getCurrentBalance(Long userId) {

        return getLastBalance(userId);
    }

    @Override
    public TransactionResponseDto withdraw(TransactionRequestDto dto, Long userId) {

        Double currentBal = getLastBalance(userId);
        Double withdrawAmt = dto.getAmount();

        if(withdrawAmt>currentBal){
            throw new InsufficientBalanceException(" Opps ! Low Balance");
        }

        Double newBal = currentBal-withdrawAmt;

        Transaction Tx = new Transaction();
        // Tx.setId(null); //auto-generated
        Tx.setUserId(userId);

        Tx.setAmount(dto.getAmount());
        Tx.setDescription(dto.getDescription());
        Tx.setType(Transaction.TransactionType.WITHDRAW);

        Tx.setTimestamp(LocalDateTime.now());
        Tx.setBalanceAfterTransaction(newBal);

        Transaction savedTransaction = transactionRepo.save(Tx);

        //send notification via feign

        NotificationRequestDto notification = new NotificationRequestDto();
        notification.setUserId(userId);
        notification.setTitle("Withdraw Successful");
        notification.setMsg("You withdraw Rs" + dto.getAmount() + " . Remaining balance: Rs" + newBal);
        notification.setType("EMAIL");

        try {
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Notification failed: " + e.getMessage());
        }

        return new TransactionResponseDto(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getDescription(),
                savedTransaction.getTimestamp(),
                savedTransaction.getBalanceAfterTransaction()
        );

    }

    @Override
    public TransactionResponseDto shopViaNetBanking(TransactionRequestDto dto, Long userId) {

        // 1 checks user exists
        if(!userClient.checkUserExists(userId)){
            throw new IllegalArgumentException("User not Found");
        }
        //  2 Validate balance
        Double currentBal = getLastBalance(userId);
        Double withdrawAmt = dto.getAmount();

        if(withdrawAmt>currentBal){
            throw new InsufficientBalanceException(" Opps ! Low Balance");
        }
        Double newBal = currentBal-withdrawAmt;


        Transaction Tx = new Transaction();
        // Tx.setId(null); //auto-generated
        Tx.setUserId(userId);

        Tx.setAmount(dto.getAmount());
        Tx.setDescription(dto.getDescription());
        Tx.setType(Transaction.TransactionType.SHOP_NET_BANKING);

        Tx.setTimestamp(LocalDateTime.now());
        Tx.setBalanceAfterTransaction(newBal);

        Transaction savedTransaction = transactionRepo.save(Tx);

        //send notification via feign

        NotificationRequestDto notification = new NotificationRequestDto();
        notification.setUserId(userId);
        notification.setTitle("Purchase via Net Banking");
        notification.setMsg("You spent  Rs " + dto.getAmount() + " . Remaining balance: Rs " + newBal);
        notification.setType("EMAIL");

        try {
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Notification failed: " + e.getMessage());
        }

        return new TransactionResponseDto(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getDescription(),
                savedTransaction.getTimestamp(),
                savedTransaction.getBalanceAfterTransaction()
        );
    }

    @Override
    public TransactionResponseDto shopViaCard(TransactionRequestDto dto, Long userId) {
        if (dto.getCardId() == null) {
            throw new IllegalArgumentException("Card ID is required for card shopping.");
        }

        boolean isCardValid = cardClient.isCardValid(dto.getCardId());
        if (!isCardValid) {
            throw new CardAlreadyBlockedException("Card is blocked or invalid.");
        }

        // Get card type (DEBIT or CREDIT)
        String cardType = cardClient.getCardType(dto.getCardId());
        Transaction.TransactionType type;

        if (cardType.equalsIgnoreCase("DEBIT")) {
            type = Transaction.TransactionType.SHOP_DEBIT_CARD;
        } else if (cardType.equalsIgnoreCase("CREDIT")) {
            type = Transaction.TransactionType.SHOP_CREDIT_CARD;
        } else {
            throw new IllegalArgumentException("Unknown card type returned: " + cardType);
        }

        Double currentBalance = getLastBalance(userId);
        if (dto.getAmount() > currentBalance) {
            throw new InsufficientBalanceException("Insufficient balance for card shopping.");
        }

        Double newBalance = currentBalance - dto.getAmount();

        Transaction tx = new Transaction(
                null,
                userId,
                dto.getAmount(),
                type,
                dto.getDescription(),
                LocalDateTime.now(),
                newBalance
        );

        Transaction saved = transactionRepo.save(tx);

        // Notify
        NotificationRequestDto notification = new NotificationRequestDto();
        notification.setUserId(userId);
        notification.setTitle("Purchase via " + cardType + " Card");

        String message = "You spent ₹" + dto.getAmount() +
                " using your " + cardType.toLowerCase() + " card." +
                " Remaining balance: ₹" + newBalance;

        notification.setMsg(message);
        notification.setType("EMAIL");

        try {
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Notification failed: " + e.getMessage());
        }

        return new TransactionResponseDto(
                saved.getId(),
                saved.getAmount(),
                saved.getType(),
                saved.getDescription(),
                saved.getTimestamp(),
                saved.getBalanceAfterTransaction()
        );
    }

    @Override
    public void settleLoanTransaction(Long userId, Double loanAmount) {
        // Check balance
        Double currentBalance = getCurrentBalance(userId);
        if (currentBalance < loanAmount) {
            throw new IllegalArgumentException("Insufficient balance to settle the loan.");
        }

        // Calculate remaining balance
        Double newBal = currentBalance - loanAmount;

        // Create transaction
        Transaction transaction = new Transaction();

        transaction.setUserId(userId);
        transaction.setAmount(loanAmount);
        transaction.setType(Transaction.TransactionType.WITHDRAW); // use WITHDRAW for loan deduction
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription("Loan Settlement");
        transaction.setBalanceAfterTransaction(newBal);

        transactionRepo.save(transaction);
    }


}
