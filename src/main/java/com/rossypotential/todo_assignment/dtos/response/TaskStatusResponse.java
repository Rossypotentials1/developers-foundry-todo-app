package com.rossypotential.todo_assignment.dtos.response;

import com.rossypotential.todo_assignment.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusResponse {
    private TaskStatus status;
}
