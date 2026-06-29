package com.bootcamp.auth.controller;

import com.bootcamp.auth.dto.APIResponseDTO;
import com.bootcamp.auth.dto.CreateEmployeeRequestDTO;
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
            @Valid @RequestBody
            CreateEmployeeRequestDTO request
    ) {

        return authService.createEmployee(request);

    }

}