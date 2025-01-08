package com.rossypotential.todo_assignment.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {
    @Size(min = 2, max = 125, message = "Firstname must be at least 2 characters long")
    @NotBlank(message = "Firstname must not be empty")
    private String firstName;

    @Size(min = 2, max = 125, message = "Lastname must be at least 2 characters long")
    @NotBlank(message = "Lastname must not be empty")
    private String lastName;


    @Size(min = 10, max = 20, message = "Firstname must be at least 2 characters long")
    @NotBlank(message = "Firstname must not be empty")
    private String phoneNumber;
}