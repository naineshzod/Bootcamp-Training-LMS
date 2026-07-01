package com.bootcamp.auth.mapper;

import com.bootcamp.auth.dto.request.CreateEmployeeRequestDTO;
import com.bootcamp.auth.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(CreateEmployeeRequestDTO dto) {

        return UserEntity.builder()
                .employeeId(dto.getEmployeeId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }

}