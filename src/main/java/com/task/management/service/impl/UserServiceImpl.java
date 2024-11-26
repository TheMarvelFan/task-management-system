package com.task.management.service.impl;

import com.task.management.model.User;
import com.task.management.repository.UserRepository;
import com.task.management.dto.request.UserLoginRequest;
import com.task.management.dto.request.UserRegistrationRequest;
import com.task.management.service.UserService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerNewUser(UserRegistrationRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException("Username is already in use.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email is already in use.");
        }

        // Create a new User object
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);

        // Save the user in the database
        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(UserLoginRequest request) {
        // Find user by email or username
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmailOrUsername())
                .or(() -> userRepository.findByUsername(request.getEmailOrUsername()));

        if (optionalUser.isEmpty()) {
            throw new ValidationException("Invalid username/email or password.");
        }

        User user = optionalUser.get();

        // Verify the password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ValidationException("Invalid username/email or password.");
        }

        return user;
    }
}
