package com.rossypotential.todo_assignment.service.impl;

import com.rossypotential.todo_assignment.dtos.request.TaskRequest;
import com.rossypotential.todo_assignment.dtos.request.UpdateTask;
import com.rossypotential.todo_assignment.dtos.response.ApiResponse;
import com.rossypotential.todo_assignment.dtos.response.TaskStatusResponse;
import com.rossypotential.todo_assignment.dtos.response.TasksResponse;
import com.rossypotential.todo_assignment.enums.TaskStatus;
import com.rossypotential.todo_assignment.exception.TaskNotFoundException;
import com.rossypotential.todo_assignment.exception.UserNotFoundException;
import com.rossypotential.todo_assignment.model.AppUser;
import com.rossypotential.todo_assignment.model.Task;
import com.rossypotential.todo_assignment.repositories.TaskRepository;
import com.rossypotential.todo_assignment.repositories.UserRepository;
import com.rossypotential.todo_assignment.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse<TasksResponse>> createTask(TaskRequest createRequest) {
        try {
            if (createRequest == null) {
                throw new IllegalArgumentException("Create request cannot be null.");
            }

            if (createRequest.getDeadline() != null && createRequest.getDeadline().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Deadline must be a future date and time.");
            }

            validateTaskRequest(createRequest);

            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            AppUser user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

            Task task = new Task();
            task.setTitle(createRequest.getTitle());
            task.setDescription(createRequest.getDescription());
            task.setDeadline(createRequest.getDeadline());
            task.setStatus(createRequest.getStatus());
            task.setUser(user);
            task = taskRepository.save(task);

            TasksResponse taskResponse = TasksResponse.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .deadline(task.getDeadline())
                    .status(task.getStatus())
                    .build();

            return ResponseEntity.ok(ApiResponse.ok("Task created Successfully", taskResponse));
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.badRequest("An error occurred while creating the task"));
        }
    }


    @Override
    public ApiResponse<TaskStatusResponse> updateTaskStatus(Long taskId, UpdateTask taskStatus) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null.");
        }
        if (taskStatus == null || taskStatus.getStatus() == null) {
            throw new IllegalArgumentException("Task status cannot be null.");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found"));

        TaskStatus currentStatus = task.getStatus();
        TaskStatus newStatus = taskStatus.getStatus();

        if (currentStatus == TaskStatus.COMPLETED) {
            throw new IllegalStateException("Cannot change the status of a completed task.");
        }

        if (currentStatus == TaskStatus.IN_PROGRESS && newStatus == TaskStatus.PENDING) {
            throw new IllegalStateException("Task cannot be moved back to PENDING from IN_PROGRESS.");
        }

        if (currentStatus == TaskStatus.PENDING && (newStatus != TaskStatus.IN_PROGRESS && newStatus != TaskStatus.COMPLETED)) {
            throw new IllegalStateException("Task in PENDING status can only move to IN_PROGRESS or COMPLETED.");
        }

        task.setStatus(newStatus);
        taskRepository.save(task);

        TaskStatusResponse response = new TaskStatusResponse(task.getStatus());
        return ApiResponse.ok("Successfully updated task STATUS",response);
    }




    @Override
    public void deleteTask(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID cannot be null.");
        }
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
        taskRepository.delete(task);
    }

    @Override
    public TasksResponse updateTask(Long taskId, TaskRequest updateRequest) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null.");
        }
        if (updateRequest == null) {
            throw new IllegalArgumentException("Update request cannot be null.");
        }

        validateTaskRequest(updateRequest);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found"));

        task.setTitle(updateRequest.getTitle());
        task.setDescription(updateRequest.getDescription());
        task.setDeadline(updateRequest.getDeadline());
        task.setStatus(updateRequest.getStatus());

        taskRepository.save(task);

        return new TasksResponse(task.getId(), task.getTitle(), task.getStatus(),task.getDescription(),task.getDeadline());
    }


    @Override
    public Page<TasksResponse> getTasksById(Long userId, int page, int size) {
        if (userId == null) {
            throw new UserNotFoundException("User ID cannot be null.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("deadline")));
        Page<Task> tasksPage = taskRepository.findByUserId(userId, pageable);

        if (tasksPage.isEmpty()) {
            return Page.empty();
        }

        return tasksPage.map(task -> {
            TaskServiceImpl.log.info("Mapping User ID: {}, Title: {}", task.getId(), task.getTitle());
            return new TasksResponse(task.getId(), task.getTitle(), task.getDescription(), task.getDeadline(), task.getStatus());
        });
    }




    private void validateTaskRequest(TaskRequest taskRequest) {
        if (taskRequest.getTitle() == null || taskRequest.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title is required.");
        }
        if (taskRequest.getDescription() == null || taskRequest.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Task description is required.");
        }
        if (taskRequest.getDeadline() == null) {
            throw new IllegalArgumentException("Task deadline is required.");
        }

    }
}
