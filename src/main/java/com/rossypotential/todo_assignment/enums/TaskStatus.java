package com.rossypotential.todo_assignment.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.rossypotential.todo_assignment.exception.InvalidTaskStatusException;

public enum TaskStatus {
    PENDING, IN_PROGRESS, COMPLETED;

    @JsonCreator
    public static TaskStatus fromValue(String value) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new InvalidTaskStatusException("Invalid TaskStatus value: " + value);
    }
}
