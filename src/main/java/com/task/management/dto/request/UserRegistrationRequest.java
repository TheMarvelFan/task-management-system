package com.task.management.dto.request;

import static com.task.management.util.Constants.ErrorMessages.PASSWORD_LENGTH_INVALID;
import static com.task.management.util.Constants.Validation.PASSWORD_MAX_LENGTH;
import static com.task.management.util.Constants.Validation.PASSWORD_MIN_LENGTH;
import static com.task.management.util.Constants.Validation.USERNAME_MAX_LENGTH;
import static com.task.management.util.Constants.Validation.USERNAME_MIN_LENGTH;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest {
    @NotBlank
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = PASSWORD_LENGTH_INVALID)
    private String password;
}

