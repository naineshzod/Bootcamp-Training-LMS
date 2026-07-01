package com.bootcamp.auth.dto.response;

import com.bootcamp.auth.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;

    private String employeeId;

    private String fullName;

    private String email;

    private Role role;

    private Boolean firstLogin;

}