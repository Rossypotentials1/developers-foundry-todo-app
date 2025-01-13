package com.rossypotential.todo_assignment.exception;
import com.rossypotential.todo_assignment.dtos.response.ApiResponse;
import com.rossypotential.todo_assignment.model.ErrorDetails;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNotSentException.class)
    public ResponseEntity<ErrorDetails> emailNotSentHandler(final EmailNotSentException ex, WebRequest webRequest){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Email Not Sent")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails>userNotFoundHandler(final UserNotFoundException ex, WebRequest webRequest){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("User Not Found")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  ResponseEntity.ok(errorDetails);
    }
    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorDetails> invalidLogin(final InvalidLoginException ex, WebRequest webRequest){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("User Not Found")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  ResponseEntity.ok(errorDetails);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage()));
    }

    @ExceptionHandler(ExpiredVerificationCodeException.class)
    public ResponseEntity<ApiResponse<?>> handleExpiredVerificationCodeException(ExpiredVerificationCodeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage()));
    }


    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidVerificationCodeException(InvalidVerificationCodeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, ex.getMessage()));
    }
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handleMessagingException(MessagingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending password reset email: " + ex.getMessage());
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidEmailFormatException(InvalidEmailFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage()));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEmailNotFoundException(EmailNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage()));
    }


    @ExceptionHandler(InvalidTaskStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidTaskStatusException(InvalidTaskStatusException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.badRequest(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "An error occurred: " + ex.getMessage()));
    }
    @ExceptionHandler(TaskNotCreatedException.class)
    public ResponseEntity<ApiResponse<?>> handleTaskNotCreatedException(TaskNotCreatedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "An error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "An error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "An error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "An error occurred: " + ex.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handle(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("MethodArgumentNotValidException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleException(ValidationException validationException) {
        ErrorDTO errorDTO;
        if (validationException instanceof ConstraintViolationException) {
            String violations = extractViolationsFromException((ConstraintViolationException) validationException);
            log.info(violations, validationException);
            errorDTO = ErrorDTO.builder()
                    .message(violations)
                    .httpStatus(HttpStatus.valueOf(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                    .build();
        } else {
            String exceptionMessage = validationException.getMessage();
            log.error(exceptionMessage, validationException);
            errorDTO = ErrorDTO.builder()
                    .httpStatus(HttpStatus.valueOf(HttpStatus.BAD_GATEWAY.getReasonPhrase()))
                    .message(exceptionMessage).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorDTO);
    }

    private String extractViolationsFromException(ConstraintViolationException validationException) {
        return validationException.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.joining("--"));
    }
}
