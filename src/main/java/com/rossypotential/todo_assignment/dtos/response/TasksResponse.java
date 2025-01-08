package com.rossypotential.todo_assignment.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rossypotential.todo_assignment.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TasksResponse {
    private Long id;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
    private TaskStatus status;

    public TasksResponse(Long id, String title, TaskStatus status, String description, LocalDateTime deadline) {
    }

    public TasksResponse(String title, Long id, String description, LocalDateTime deadline, TaskStatus status) {
    }
}