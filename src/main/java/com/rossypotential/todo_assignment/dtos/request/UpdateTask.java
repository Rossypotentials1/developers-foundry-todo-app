package com.rossypotential.todo_assignment.dtos.request;

import com.rossypotential.todo_assignment.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request payload to update a task")
public class UpdateTask {
    @Schema(description = "The status of the task. Allowed values: PENDING, IN_PROGRESS, COMPLETED")
    private TaskStatus status;

}
