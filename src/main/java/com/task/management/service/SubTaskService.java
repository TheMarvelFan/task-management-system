package com.task.management.service;

import org.springframework.data.domain.Page;

import com.task.management.dto.response.SubTaskResponse;

public interface SubTaskService {
    SubTaskResponse createSubTask(Long taskId, Long userId, String description);
    SubTaskResponse updateSubTaskStatus(Long subTaskId, Integer status, Long userId);
    void deleteSubTask(Long subTaskId, Long userId);
    Page<SubTaskResponse> getAllSubTasks(Long userId, Long taskId, int page, int size);
    SubTaskResponse getSubTaskById(Long subTaskId, Long userId);
}
