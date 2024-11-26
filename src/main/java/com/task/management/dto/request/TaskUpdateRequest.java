package com.task.management.dto.request;

import com.task.management.model.TaskStatus;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskUpdateRequest {
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    private TaskStatus status;
}
