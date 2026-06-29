package com.bootcamp.auth.service;

import com.bootcamp.auth.dto.CreateEmployeeRequestDTO;
import com.bootcamp.auth.dto.APIResponseDTO;
import com.bootcamp.auth.entity.UserEntity;
import com.bootcamp.auth.exception.UserAlreadyExistsException;
import com.bootcamp.auth.mapper.UserMapper;
import com.bootcamp.auth.repository.UserRepository;
import com.bootcamp.auth.util.PasswordGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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

        return APIResponseDTO.<String>builder()
                .success(true)
                .message("Employee created successfully")
                .data(user.getEmployeeId())
                .build();

    }

}