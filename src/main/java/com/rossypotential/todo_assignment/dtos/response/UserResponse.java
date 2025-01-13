package com.rossypotential.todo_assignment.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rossypotential.todo_assignment.model.ErrorDetails;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse<T>  {
    private String responseMessage;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long id;

    private T data;

    public UserResponse(Long id,String responseMessage, String firstName, String lastName, String email, String phoneNumber, T data) {
        this.id = id;
        this.responseMessage = responseMessage;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.data = data;
    }

    public UserResponse(String firstName, String lastName, String email, String phoneNumber, T data) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.data = data;

    }

    public UserResponse(String responseMessage, ErrorDetails errorDetails) {
        this.responseMessage = responseMessage;
    }

    public UserResponse(String message, String fileUrl) {
        this.responseMessage = message;
        this.data = (T) fileUrl;
    }

    public UserResponse(String message) {
        this.responseMessage = message;
    }

    public UserResponse(Long id, @Email @Email String email, String firstName, String lastName) {
    }

    public String getData() {
        return data != null ? data.toString() : null;
    }
}
