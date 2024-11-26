package com.task.management.dto.request;

import static com.task.management.util.Constants.ErrorMessages.DESCRIPTION_LENGTH_INVALID;
import static com.task.management.util.Constants.ErrorMessages.TITLE_LENGTH_INVALID;
import static com.task.management.util.Constants.Validation.DESCRIPTION_MAX_LENGTH;
import static com.task.management.util.Constants.Validation.TITLE_MAX_LENGTH;
import static com.task.management.util.Constants.Validation.TITLE_MIN_LENGTH;

import com.task.management.model.Priority;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_LENGTH_INVALID)
    private String title;

    @Size(max = DESCRIPTION_MAX_LENGTH, message = DESCRIPTION_LENGTH_INVALID)
    private String description;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    @NotNull(message = "Priority is required")
    private Priority priority;
}
