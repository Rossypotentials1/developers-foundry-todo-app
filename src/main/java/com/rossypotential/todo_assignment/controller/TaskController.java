package com.rossypotential.todo_assignment.controller;

import com.rossypotential.todo_assignment.dtos.request.TaskRequest;
import com.rossypotential.todo_assignment.dtos.request.UpdateTask;
import com.rossypotential.todo_assignment.dtos.response.ApiResponse;
import com.rossypotential.todo_assignment.dtos.response.TaskStatusResponse;
import com.rossypotential.todo_assignment.dtos.response.TasksResponse;
import com.rossypotential.todo_assignment.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/create-todo")
    public ResponseEntity<ApiResponse<TasksResponse>> createTask(@RequestBody TaskRequest createRequest) {
        if (createRequest == null) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest("Create request cannot be null."));
        }
        return taskService.createTask(createRequest);
    }


    @PutMapping("/update-todo/{taskId}")
    public ApiResponse<Void> updateTask(@PathVariable Long taskId, @RequestBody TaskRequest updateRequest) {
        taskService.updateTask(taskId, updateRequest);
        return ApiResponse.ok("tasks updated successfully",null);
    }

    @PatchMapping("/update-status/{taskId}")
    public ResponseEntity<ApiResponse<TaskStatusResponse>> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody UpdateTask taskStatus) {
        ApiResponse<TaskStatusResponse> response = taskService.updateTaskStatus(taskId, taskStatus);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete-todo/{taskId}")
    public ApiResponse<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ApiResponse.ok("Deleted Successfully",null);
    }

    @GetMapping("/get-todo-by-user/{userId}")
    public ResponseEntity<ApiResponse<Page<TasksResponse>>> getTasksById(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @PathVariable Long userId) {
        try {
            Page<TasksResponse> tasksResponse = taskService.getTasksById(userId, page, size);
            return ResponseEntity.ok(ApiResponse.ok("All your TODOs;",tasksResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest(e.getMessage()));
        }
    }
}

