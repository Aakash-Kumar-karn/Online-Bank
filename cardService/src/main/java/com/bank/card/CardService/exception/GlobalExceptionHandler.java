package com.bank.card.CardService.exception;

import com.bank.card.CardService.dto.CardDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> cardNotFoundException(CardNotFoundException ex, HttpServletRequest req){
     return buildResponse(HttpStatus.NOT_FOUND,ex.getMessage(),req.getRequestURI());
    }

    @ExceptionHandler(CardAlreadyBlockedException.class)
    public ResponseEntity<ApiErrorResponse> handleAlreadyBlocked(CardAlreadyBlockedException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }
    @ExceptionHandler(CardLimitUpgradeException.class)
    public ResponseEntity<ApiErrorResponse> handleUpgrade(CardLimitUpgradeException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest req
    ) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessage
                    .append(fieldError.getField())
                    .append(" - ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }

        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage.toString(), req.getRequestURI());
    }
    @ExceptionHandler(RequestAlreadyProcessedException.class)
    public ResponseEntity<ApiErrorResponse> handleRequestAlreadyProcessedException(CardLimitUpgradeException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAll(Exception ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", req.getRequestURI());
    }
    @ExceptionHandler(TokenMissingException.class)
    public ResponseEntity<ApiErrorResponse> handleMissing(TokenMissingException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiErrorResponse> handleExpired(TokenExpiredException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(TokenMalformedException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformed(TokenMalformedException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message, String path) {
        ApiErrorResponse error = new ApiErrorResponse(LocalDateTime.now(),  message,status.value(), path);
        return new ResponseEntity<>(error, status);
    }

}
