package com.rossypotential.todo_assignment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rossypotential.todo_assignment.dtos.request.AuthenticationRequest;
import com.rossypotential.todo_assignment.dtos.request.RegisterRequest;
import com.rossypotential.todo_assignment.dtos.response.ApiResponse;
import com.rossypotential.todo_assignment.dtos.response.AuthenticationResponse;
import com.rossypotential.todo_assignment.dtos.response.RegisterResponse;
import com.rossypotential.todo_assignment.dtos.response.UserResponse;
import com.rossypotential.todo_assignment.model.AppUser;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public ApiResponse<RegisterResponse> register(RegisterRequest registerRequest) throws MessagingException, JsonProcessingException;
    public ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest request);
    public String verifyEmail(String token);
    public ResponseEntity<ApiResponse<String>> resendEmailVerification(String email);
    ResponseEntity<ApiResponse<UserResponse<?>>>  editUser(Long id, String firstName, String lastName, String phoneNumber);
    UserResponse<?> viewUser(Long id);
    public UserDetails loadUserByUsername(String username);
    public Page<UserResponse<?>> getAllUsers(int page, int size);

    }
