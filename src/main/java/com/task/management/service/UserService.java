package com.task.management.service;

import com.task.management.dto.request.UserLoginRequest;
import com.task.management.dto.request.UserRegistrationRequest;
import com.task.management.model.User;

public interface UserService {
    User registerNewUser(UserRegistrationRequest request);
    User authenticateUser(UserLoginRequest request);
}
