package com.bootcamp.auth.controller;

import com.bootcamp.auth.dto.response.UserProfileResponseDTO;
import org.springframework.security.core.Authentication;
import com.bootcamp.auth.dto.request.*;
import com.bootcamp.auth.dto.response.APIResponseDTO;
import com.bootcamp.auth.dto.response.LoginResponseDTO;
import com.bootcamp.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/employees")
    public APIResponseDTO<String> createEmployee(
            @Valid
            @RequestBody
            CreateEmployeeRequestDTO request
    ) {

        return authService.createEmployee(request);

    }

    @GetMapping("/profile")
    public APIResponseDTO<UserProfileResponseDTO> getProfile(
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return APIResponseDTO
                .<UserProfileResponseDTO>builder()
                .success(true)
                .message("Profile fetched successfully")
                .data(
                        authService.getProfile(email)
                )
                .build();

    }

    @PostMapping("/login")
    public APIResponseDTO<LoginResponseDTO> login(
            @Valid
            @RequestBody
            LoginRequestDTO request
    ) {

        return APIResponseDTO.<LoginResponseDTO>builder()
                .success(true)
                .message("Login successful")
                .data(authService.login(request))
                .build();

    }

    @PostMapping("/change-password")
    public APIResponseDTO<String> changePassword(
            @Valid
            @RequestBody
            ChangePasswordRequestDTO request
    ) {

        return APIResponseDTO.<String>builder()
                .success(true)
                .message(authService.changePassword(request))
                .data(authService.changePassword(request))
                .build();

    }

    @PostMapping("/forgot-password")
    public APIResponseDTO<String> forgotPassword(

            @Valid
            @RequestBody
            ForgotPasswordRequestDTO request

    ) {

        return APIResponseDTO.<String>builder()
                .success(true)
                .message(
                        authService.forgotPassword(request)
                )
                .data(null)
                .build();

    }


    @PostMapping("/reset-password")
    public APIResponseDTO<String> resetPassword(

            @Valid
            @RequestBody
            ResetPasswordRequestDTO request

    ) {

        return APIResponseDTO.<String>builder()
                .success(true)
                .message(
                        authService.resetPassword(request)
                )
                .data(null)
                .build();

    }
}