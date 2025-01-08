package com.rossypotential.todo_assignment.dtos.request;

import com.rossypotential.todo_assignment.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTask {
    private TaskStatus status;

}
