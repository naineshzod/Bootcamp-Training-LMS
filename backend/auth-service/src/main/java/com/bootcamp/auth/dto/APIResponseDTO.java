package com.bootcamp.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class APIResponseDTO<T> {

    private Boolean success;

    private String message;

    private T data;

}