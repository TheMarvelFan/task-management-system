package com.task.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotBlank
    private String emailOrUsername;

    @NotBlank
    private String password;
}
