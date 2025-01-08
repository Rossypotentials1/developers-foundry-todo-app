package com.rossypotential.todo_assignment.service;


import com.rossypotential.todo_assignment.dtos.request.TaskRequest;
import com.rossypotential.todo_assignment.dtos.request.UpdateTask;
import com.rossypotential.todo_assignment.dtos.response.ApiResponse;
import com.rossypotential.todo_assignment.dtos.response.TaskStatusResponse;
import com.rossypotential.todo_assignment.dtos.response.TasksResponse;
import com.rossypotential.todo_assignment.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface TaskService {

    public void deleteTask(Long id);
    public TasksResponse updateTask(Long taskId, TaskRequest updateRequest);
    public ResponseEntity<ApiResponse<TasksResponse>> createTask(TaskRequest createRequest);
    public ApiResponse<TaskStatusResponse> updateTaskStatus(Long taskId, UpdateTask taskStatus);
    public Page<TasksResponse> getTasksById(Long userId, int page, int size);

}