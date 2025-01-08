package com.rossypotential.todo_assignment.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Size(min = 2, max = 125, message = "Firstname must be at least 2 characters long")
    @NotBlank(message = "Firstname must not be empty")
    @Schema(description = "User's first name", example = "Rose")
    private String firstName;

    @Size(min = 2, max = 125, message = "Lastname must be at least 2 characters long")
    @NotBlank(message = "Lastname must not be empty")
    @Schema(description = "User's last name", example = "Mary")
    private String lastName;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email must be in a valid format with a valid domain"
    )
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;


    @NotBlank(message = "Phone number must not be empty")
    @Schema(description = "User's phone number", example = "+2348140996323")
    @Pattern(regexp = "^(\\+\\d{10,13}|\\d{11}|\\+\\d{14})$", message = "Phone number must be (digits) either 11 digits or 14 digits (with optional + at the beginning).")
    private String phoneNumber;




    @NotBlank(message = "Password must not be empty")
    @Schema(description = "User's password", example = "MySecureP@ssw0rd")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must be at least 8 characters long, contain one uppercase letter, one lowercase letter, one number, and one special character."
    )
    private String password;
}
