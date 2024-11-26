package com.task.management.controller;

import static com.task.management.util.Constants.Endpoints.TASKS_PATH;

import com.task.management.dto.request.TaskRequest;
import com.task.management.dto.request.TaskUpdateRequest;
import com.task.management.dto.response.TaskResponse;
import com.task.management.model.Priority;
import com.task.management.model.TaskStatus;
import com.task.management.model.User;
import com.task.management.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(TASKS_PATH)
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskResponse response = taskService.createTask(request, getUserId(userDetails));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskResponse response = taskService.updateTask(taskId, request, getUserId(userDetails));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Map<String, String>> deleteTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal UserDetails userDetails) {
        taskService.deleteTask(taskId, getUserId(userDetails));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Task with ID " + taskId + " has been deleted");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal UserDetails userDetails) {
        TaskResponse response = taskService.getTask(taskId, getUserId(userDetails));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getTasks(
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            @RequestParam(required = false) Integer subtasksCount,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Retrieve user ID
        Long userId = getUserId(userDetails);

        // Fetch tasks with pagination using the service
        Page<TaskResponse> tasks = taskService.findTasksByCriteria(
                userId, priority, dueDate, subtasksCount, title, status, createdAt, updatedAt, page, size);

        return ResponseEntity.ok(tasks);
    }




    private Long getUserId(UserDetails userDetails) {
        return ((User) userDetails).getId();
    }
}
