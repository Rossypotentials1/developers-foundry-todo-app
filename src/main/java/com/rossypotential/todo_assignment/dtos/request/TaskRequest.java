package com.rossypotential.todo_assignment.dtos.request;

import com.rossypotential.todo_assignment.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    @NotEmpty(message = "Please provide title")
    @Column(nullable = false)
    private String title;
    private String description;
    private LocalDateTime deadline;
    @Schema(hidden = true)
    private TaskStatus status;
}