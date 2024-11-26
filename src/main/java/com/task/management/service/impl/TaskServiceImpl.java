package com.task.management.service.impl;

import com.task.management.dto.request.TaskRequest;
import com.task.management.dto.request.TaskUpdateRequest;
import com.task.management.dto.response.SubTaskResponse;
import com.task.management.dto.response.TaskResponse;
import com.task.management.exception.CustomException;
import com.task.management.model.Priority;
import com.task.management.model.Task;
import com.task.management.model.TaskStatus;
import com.task.management.model.User;
import com.task.management.repository.TaskRepository;
import com.task.management.repository.UserRepository;
import com.task.management.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest request, Long userId) {
        User user = getUserById(userId);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .priority(request.getPriority())
                .user(user)
                .status(TaskStatus.TODO)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToTaskResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request, Long userId) {
        Task task = getTaskByIdAndUserId(taskId, userId);

        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        Task updatedTask = taskRepository.save(task);
        return mapToTaskResponse(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId, Long userId) {
        Task task = getTaskByIdAndUserId(taskId, userId);
        task.setIsDeleted((byte) 1);
        task.getSubTasks().forEach(subTask -> subTask.setIsDeleted(true));
        taskRepository.save(task);
    }

    @Override
    public TaskResponse getTask(Long taskId, Long userId) {
        Task task = getTaskByIdAndUserId(taskId, userId);
        return mapToTaskResponse(task);
    }

    @Override
    public Page<TaskResponse> getAllUserTasks(Long userId, Priority priority, LocalDate dueDate, Pageable pageable) {
        Page<Task> tasks;
        if (priority != null && dueDate != null) {
            tasks = taskRepository.findByUserIdAndPriorityAndDueDateBefore(userId, priority, dueDate, pageable);
        } else {
            tasks = taskRepository.findAllByUserId(userId, pageable);
        }
        return tasks.map(this::mapToTaskResponse);
    }

    private Task getTaskByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new CustomException("Task not found", HttpStatus.NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Page<TaskResponse> findTasksByCriteria(
            Long userId,
            Priority priority,
            LocalDate dueDate,
            Integer subtasksCount,
            String title,
            TaskStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size);

        return taskRepository.findTasksByCriteria(userId, priority, dueDate, subtasksCount, title, status, createdAt, updatedAt, pageable)
                .map(this::mapToTaskResponse); // Map each entity to a response DTO
    }



    private TaskResponse mapToTaskResponse(Task task) {
        if (task.getSubTasks() == null) {
            task.setSubTasks(new ArrayList<>());
        }
        long completedSubTasks = task.getSubTasks().stream()
                .filter(subTask -> !subTask.getIsDeleted() && subTask.getStatus() == 1)
                .count();
        long totalSubTasks = task.getSubTasks().stream()
                .filter(subTask -> !subTask.getIsDeleted())
                .count();

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .completedSubTasks(completedSubTasks)
                .totalSubTasks(totalSubTasks)
                .subTasks(task.getSubTasks().stream()
                        .filter(subTask -> !subTask.getIsDeleted())
                        .map(subTask -> SubTaskResponse.builder()
                                .id(subTask.getId())
                                .taskId(task.getId())
                                .status(subTask.getStatus())
                                .build())
                        .collect(Collectors.toList()))
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
