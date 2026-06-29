package com.bootcamp.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {

    private String accessToken;

    private String refreshToken;

    private Boolean firstLogin;

    private String role;

}