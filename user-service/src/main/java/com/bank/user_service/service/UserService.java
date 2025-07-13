package com.bank.user_service.service;

import com.bank.user_service.dto.*;

public interface UserService {

    UserDto registerUser(UserDto userDto);
    LoginResponse login(LoginRequestDto loginRequest);

    void changePassword(String email, ChangePasswordRequestDto dto);

    void forgotPassword(ForgotPasswordRequestDto dto);
    void resetPassword(ResetPasswordRequestDto dto);

    boolean existsById(Long userId);  // for transaction ms
}

