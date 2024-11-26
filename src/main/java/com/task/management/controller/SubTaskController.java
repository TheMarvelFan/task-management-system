package com.task.management.controller;

import static com.task.management.util.Constants.Endpoints.SUBTASKS_PATH;

import java.util.Map;

import com.task.management.dto.response.SubTaskResponse;
import com.task.management.model.User;
import com.task.management.service.SubTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping(SUBTASKS_PATH)
@RequiredArgsConstructor
public class SubTaskController {
    private final SubTaskService subTaskService;

    @PostMapping("/task/{taskId}")
    public ResponseEntity<SubTaskResponse> createSubTask(
            @PathVariable Long taskId,
            @RequestBody String description,
            @AuthenticationPrincipal UserDetails userDetails) {
        SubTaskResponse response = subTaskService.createSubTask(taskId, getUserId(userDetails), description);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{subTaskId}/status/{status}")
    public ResponseEntity<SubTaskResponse> updateSubTaskStatus(
            @PathVariable Long subTaskId,
            @PathVariable Integer status,
            @AuthenticationPrincipal UserDetails userDetails) {
        SubTaskResponse response = subTaskService.updateSubTaskStatus(
                subTaskId, status, getUserId(userDetails));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{subTaskId}")
    public ResponseEntity<Map<String, String>> deleteSubTask(
            @PathVariable Long subTaskId,
            @AuthenticationPrincipal UserDetails userDetails) {
        subTaskService.deleteSubTask(subTaskId, getUserId(userDetails));
        return ResponseEntity.ok(Map.of("message", "SubTask with ID " + subTaskId + " deleted successfully"));
    }

    @GetMapping("/{subTaskId}")
    public ResponseEntity<SubTaskResponse> getSubTaskById(
            @PathVariable Long subTaskId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = getUserId(userDetails);

        SubTaskResponse response = subTaskService.getSubTaskById(subTaskId, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<SubTaskResponse>> getAllSubTasks(
            @RequestParam(required = false) Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Retrieve user ID
        Long userId = getUserId(userDetails);

        // Fetch subtasks with pagination
        Page<SubTaskResponse> response = subTaskService.getAllSubTasks(userId, taskId, page, size);

        return ResponseEntity.ok(response);
    }

    private Long getUserId(UserDetails userDetails) {
        return ((User) userDetails).getId();
    }
}
