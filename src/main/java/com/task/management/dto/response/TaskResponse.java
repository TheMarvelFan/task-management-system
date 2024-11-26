package com.task.management.dto.response;

import com.task.management.model.Priority;
import com.task.management.model.TaskStatus;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private TaskStatus status;
    private Long completedSubTasks;
    private Long totalSubTasks;
    private List<SubTaskResponse> subTasks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
