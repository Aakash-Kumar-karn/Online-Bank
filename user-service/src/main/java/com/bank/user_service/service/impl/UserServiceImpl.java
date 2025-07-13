package com.bank.user_service.service.impl;
import com.bank.user_service.dto.*;
import com.bank.user_service.exception.EmailAlreadyExistsException;
import com.bank.user_service.exception.InvalidPasswordException;
import com.bank.user_service.exception.TokenExpiredException;
import com.bank.user_service.exception.UserNotFoundException;
import com.bank.user_service.model.PasswordResetToken;
import com.bank.user_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.bank.user_service.repository.PasswordResetTokenRepository;
import com.bank.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bank.user_service.security.JwtUtil;
import com.bank.user_service.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserDto registerUser(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");

        }

        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Hash password
        user.setMobile(userDto.getMobile());

        user.setRole("ROLE_USER");
        user.setActive(true);

        User savedUser = userRepository.save(user);

        return new UserDto(
                savedUser.getFullName(),
                savedUser.getEmail(),
                "", //Mask password
                savedUser.getMobile());
    }

    @Override
    public LoginResponse login(LoginRequestDto loginRequest) {

        //find user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email / passwrd "));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invlaid password");
        }

        //generate jwt token
        String token = jwtUtil.generateToken(user);

        UserDto userDto = new UserDto(
                user.getFullName(),
                user.getEmail(),
                "",//hide password
                user.getMobile()
        );

        return new LoginResponse(token, "Login successful", userDto);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequestDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        //update new password -> hashed
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }


    @Override
    public void forgotPassword(ForgotPasswordRequestDto dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(" Email not register !! "));




        //generate token
        String token = UUID.randomUUID().toString();
        Date expiry = new Date(System.currentTimeMillis() + 1000 * 60 * 15); // 15 min expiry

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(expiry);

        passwordResetTokenRepository.save(resetToken);

        // Send reset link (mocked for now)
        String resetLink = "http://localhost:8081/api/users/reset-password?token=" + token;
        System.out.println("🔗 Reset Password Link: " + resetLink);

    }


    @Override
    public void resetPassword(ResetPasswordRequestDto dto) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new InvalidPasswordException("Invalid reset token"));

        if(resetToken.getExpiryDate().before(new Date())){
            throw new TokenExpiredException("Reset token expired");
        }

        User user = resetToken.getUser(); // kaun se user ka pswd reset hoga
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        // Invalidate token
        passwordResetTokenRepository.delete(resetToken);
    }

    @Override
    public boolean existsById(Long userId) {

        return userRepository.existsById(userId);
    }


}