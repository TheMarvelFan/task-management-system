package com.task.management.controller;

import static com.task.management.util.Constants.Endpoints.AUTH_PATH;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.management.config.JwtService;
import com.task.management.dto.request.UserLoginRequest;
import com.task.management.dto.request.UserRegistrationRequest;
import com.task.management.model.User;
import com.task.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(AUTH_PATH)
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        User user = userService.registerNewUser(request);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequest request) {
        User user = userService.authenticateUser(request);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
