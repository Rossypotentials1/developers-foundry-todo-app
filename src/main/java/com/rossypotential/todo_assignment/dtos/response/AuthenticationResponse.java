package com.rossypotential.todo_assignment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String email;
    private String firstName;
    private String accessToken;
    private String refreshToken;
}
