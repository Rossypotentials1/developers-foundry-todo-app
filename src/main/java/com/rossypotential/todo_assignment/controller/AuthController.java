package com.rossypotential.todo_assignment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rossypotential.todo_assignment.dtos.request.AuthenticationRequest;
import com.rossypotential.todo_assignment.dtos.request.RegisterRequest;
import com.rossypotential.todo_assignment.dtos.response.ApiResponse;
import com.rossypotential.todo_assignment.dtos.response.AuthenticationResponse;
import com.rossypotential.todo_assignment.dtos.response.RegisterResponse;
import com.rossypotential.todo_assignment.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for managing user authentication")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "Register a new user", description = "Register a new user with email, password, and other details.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest registerRequest) throws MessagingException, JsonProcessingException {
        ApiResponse<RegisterResponse> apiResponse = userService.register(registerRequest);
        if (!apiResponse.isSuccess()) {
            return ResponseEntity.status(apiResponse.getResponseCode()).body(apiResponse);
        }
        return ResponseEntity.ok(apiResponse);
    }



    @Operation(summary = "Authenticate a user", description = "Authenticate a user and return an access token.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest request) {
        ApiResponse<AuthenticationResponse> response = userService.authenticate(request);
        return ResponseEntity.ok(response);

    }


    @Operation(summary = "Verify email", description = "Verify the user's email using a token.")
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam(name = "verificationCode") String verificationCode) {
        return ResponseEntity.ok(userService.verifyEmail(verificationCode));
    }



    @Operation(summary = "Resend email verification", description = "Resend the email verification link to the user's email.")
    @PostMapping("/users/resend-email")
    public ResponseEntity<ApiResponse<String>> resendEmailVerification(@RequestParam String email) {
        return userService.resendEmailVerification(email);

    }
}
