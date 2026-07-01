package com.bootcamp.auth.dto.response;

import com.bootcamp.auth.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDTO {

    private String employeeId;

    private String fullName;

    private String email;

    private Role role;

    private Boolean firstLogin;

    private Boolean enabled;

}