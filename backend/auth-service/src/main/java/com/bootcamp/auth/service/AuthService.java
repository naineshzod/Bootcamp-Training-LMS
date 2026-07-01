package com.bootcamp.auth.service;

import com.bootcamp.auth.dto.request.*;
import com.bootcamp.auth.dto.response.APIResponseDTO;
import com.bootcamp.auth.dto.response.LoginResponseDTO;
import com.bootcamp.auth.entity.UserEntity;
import com.bootcamp.auth.exception.InvalidCredentialsException;
import com.bootcamp.auth.exception.ResourceNotFoundException;
import com.bootcamp.auth.exception.UserAlreadyExistsException;
import com.bootcamp.auth.mapper.UserMapper;
import com.bootcamp.auth.repository.UserRepository;
import com.bootcamp.auth.util.JwtUtil;
import com.bootcamp.auth.util.PasswordGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bootcamp.auth.util.OtpGeneratorUtil;
import com.bootcamp.auth.dto.response.UserProfileResponseDTO;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public APIResponseDTO<String> createEmployee(
            CreateEmployeeRequestDTO request
    ) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new UserAlreadyExistsException(
                    "Email already exists"
            );

        }

        if (userRepository.existsByEmployeeId(request.getEmployeeId())) {

            throw new UserAlreadyExistsException(
                    "Employee ID already exists"
            );

        }

        String temporaryPassword =
                PasswordGeneratorUtil.generatePassword(10);

        UserEntity user =
                userMapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(temporaryPassword)
        );

        userRepository.save(user);

        emailService.sendWelcomeMail(
                user.getEmail(),
                user.getFullName(),
                temporaryPassword
        );

        log.info(
                "Employee created successfully: {}",
                user.getEmployeeId()
        );

        return APIResponseDTO.<String>builder()
                .success(true)
                .message("Employee created successfully")
                .data(user.getEmployeeId())
                .build();

    }

    public UserProfileResponseDTO getProfile(
            String email
    ) {

        UserEntity user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return UserProfileResponseDTO.builder()
                .employeeId(user.getEmployeeId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .firstLogin(user.getFirstLogin())
                .enabled(user.getEnabled())
                .build();

    }

    public LoginResponseDTO login(
            LoginRequestDTO request
    ) {

        UserEntity user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                    "Invalid email or password"
            );

        }

        String token =
                jwtUtil.generateToken(user.getEmail());

        log.info(
                "User logged in successfully: {}",
                user.getEmail()
        );

        return LoginResponseDTO.builder()
                .token(token)
                .employeeId(user.getEmployeeId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .firstLogin(user.getFirstLogin())
                .build();

    }
    public String forgotPassword(
            ForgotPasswordRequestDTO request
    ) {

        UserEntity user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        String otp =
                OtpGeneratorUtil.generateOtp();

        user.setOtp(otp);

        user.setOtpExpiryTime(
                LocalDateTime.now().plusMinutes(5)
        );

        userRepository.save(user);

        emailService.sendOtpMail(
                user.getEmail(),
                user.getFullName(),
                otp
        );

        log.info(
                "Password reset OTP sent to: {}",
                user.getEmail()
        );

        return "OTP sent successfully";

    }

    public String resetPassword(
            ResetPasswordRequestDTO request
    ) {

        UserEntity user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        if (!request.getOtp().equals(user.getOtp())) {

            throw new InvalidCredentialsException(
                    "Invalid OTP"
            );

        }

        if (LocalDateTime.now()
                .isAfter(user.getOtpExpiryTime())) {

            throw new InvalidCredentialsException(
                    "OTP has expired"
            );

        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        user.setOtp(null);
        user.setOtpExpiryTime(null);

        userRepository.save(user);

        log.info(
                "Password reset successfully: {}",
                user.getEmail()
        );

        return "Password reset successfully";

    }

    public String changePassword(
            ChangePasswordRequestDTO request
    ) {

        UserEntity user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword()
        )) {

            throw new InvalidCredentialsException(
                    "Old password is incorrect"
            );

        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        user.setFirstLogin(false);

        userRepository.save(user);

        log.info(
                "Password changed successfully for: {}",
                user.getEmail()
        );

        return "Password changed successfully";

    }

}