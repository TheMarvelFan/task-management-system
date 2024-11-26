package com.task.management.service.impl;

import com.task.management.dto.response.SubTaskResponse;
import com.task.management.exception.CustomException;
import com.task.management.model.SubTask;
import com.task.management.model.Task;
import com.task.management.repository.SubTaskRepository;
import com.task.management.repository.TaskRepository;
import com.task.management.service.SubTaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubTaskServiceImpl implements SubTaskService {
    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public SubTaskResponse createSubTask(Long taskId, Long userId, String description) {
        Task task = getTaskByIdAndUserId(taskId, userId);

        SubTask subTask = SubTask.builder()
                .task(task)
                .status(0)
                .description(description)
                .isDeleted(false)
                .build();

        SubTask savedSubTask = subTaskRepository.save(subTask);
        return mapToSubTaskResponse(savedSubTask);
    }

    @Override
    @Transactional
    public SubTaskResponse updateSubTaskStatus(Long subTaskId, Integer status, Long userId) {
        if (status != 0 && status != 1) {
            throw new CustomException("Invalid status value. Must be 0 or 1", HttpStatus.BAD_REQUEST);
        }

        SubTask subTask = getSubTaskByIdAndUserId(subTaskId, userId);
        subTask.setStatus(status);
        SubTask updatedSubTask = subTaskRepository.save(subTask);

        return mapToSubTaskResponse(updatedSubTask);
    }

    @Override
    @Transactional
    public void deleteSubTask(Long subTaskId, Long userId) {
        SubTask subTask = getSubTaskByIdAndUserId(subTaskId, userId);
        subTask.setIsDeleted(true);
        subTaskRepository.save(subTask);
    }

    @Override
    public Page<SubTaskResponse> getAllSubTasks(Long userId, Long taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (taskId != null) {
            // Verify task belongs to user
            getTaskByIdAndUserId(taskId, userId);
            return subTaskRepository.findAllByTaskId(taskId, pageable)
                    .map(this::mapToSubTaskResponse); // Map entities to DTOs
        } else {
            return subTaskRepository.findAllByUserId(userId, pageable)
                    .map(this::mapToSubTaskResponse); // Map entities to DTOs
        }
    }

    @Override
    public SubTaskResponse getSubTaskById(Long subTaskId, Long userId) {
        SubTask subTask = subTaskRepository.findByIdAndUserId(subTaskId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Subtask not found or does not belong to the user"));

        return mapToSubTaskResponse(subTask);
    }


    private Task getTaskByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new CustomException("Task not found", HttpStatus.NOT_FOUND));
    }

    private SubTask getSubTaskByIdAndUserId(Long subTaskId, Long userId) {
        return subTaskRepository.findByIdAndUserId(subTaskId, userId)
                .orElseThrow(() -> new CustomException("SubTask not found", HttpStatus.NOT_FOUND));
    }

    private SubTaskResponse mapToSubTaskResponse(SubTask subTask) {
        return SubTaskResponse.builder()
                .id(subTask.getId())
                .taskId(subTask.getTask().getId())
                .status(subTask.getStatus())
                .description(subTask.getDescription())
                .build();
    }
}
