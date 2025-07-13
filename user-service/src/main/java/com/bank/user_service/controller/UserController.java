package com.bank.user_service.controller;

import com.bank.user_service.dto.*;
import com.bank.user_service.service.UserService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable Long userId) {
        boolean exists = userService.existsById(userId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid  @RequestBody UserDto userDto){
        UserDto registered = userService.registerUser(userDto);
        return ResponseEntity.ok(registered);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto loginRequest){
        LoginResponse response = userService.login(loginRequest);
        System.out.println("Login request received: " + loginRequest.getEmail());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequestDto changePasswordDto,
            Authentication authentication
            ){
        //Extract email from authenticated principal (JWT handled by filter)
        String email = authentication.getName();
        userService.changePassword(email,changePasswordDto);
        return new ResponseEntity<>("Password updated successfully",HttpStatus.OK) ;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgetPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto){
        userService.forgotPassword(forgotPasswordRequestDto);
        return ResponseEntity.ok("Password reset link sent to email (printed in console)");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDto dto){
        userService.resetPassword(dto);
        return new ResponseEntity<>("Password has been reset succesful",HttpStatus.OK);
    }

    //admin only -> end-point
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only")
    public ResponseEntity<String> adminOnly(){

        return ResponseEntity.ok("Only admin can this");
    }

    // ANY LOGGED-IN USER
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<String> profile() {
        return ResponseEntity.ok("Profile access success");
    }

}
