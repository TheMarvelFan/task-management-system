package com.task.management.service;

import com.task.management.dto.request.TaskRequest;
import com.task.management.dto.request.TaskUpdateRequest;
import com.task.management.dto.response.TaskResponse;
import com.task.management.model.Priority;
import com.task.management.model.TaskStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TaskService {
    TaskResponse createTask(TaskRequest request, Long userId);
    TaskResponse updateTask(Long taskId, TaskUpdateRequest request, Long userId);
    void deleteTask(Long taskId, Long userId);
    TaskResponse getTask(Long taskId, Long userId);
    Page<TaskResponse> getAllUserTasks(Long userId, Priority priority, LocalDate dueDate, Pageable pageable);
    Page<TaskResponse> findTasksByCriteria(
        Long userId,
        Priority priority,
        LocalDate dueDate,
        Integer subtasksCount,
        String title,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int page,
        int size
    );
}
