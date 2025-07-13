package com.bank.user_service.exception;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private LocalDateTime localDateTime;
    private String message;
    private int status;
    private String path;
}
