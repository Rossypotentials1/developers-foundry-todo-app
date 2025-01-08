package com.rossypotential.todo_assignment.dtos.request;

import com.rossypotential.todo_assignment.enums.TaskStatus;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private String title;
    private String description;
    private LocalDateTime deadline;
    private TaskStatus status;
}