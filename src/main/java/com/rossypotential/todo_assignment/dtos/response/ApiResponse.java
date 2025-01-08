package com.rossypotential.todo_assignment.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    private boolean success;
    private Integer responseCode;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok (String message,T data) {
        return ApiResponse.<T>builder()
                .responseCode(200)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
    public static <T> ApiResponse<T> badRequest (String message) {
        return ApiResponse.<T>builder()
                .responseCode(400)
                .success(false)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> unauthorized (String message) {
        return ApiResponse.<T>builder()
                .responseCode(401)
                .success(false)
                .message("unauthorized")
                .build();
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
