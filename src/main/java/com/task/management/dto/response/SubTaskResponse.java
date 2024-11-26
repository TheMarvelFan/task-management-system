package com.task.management.dto.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class SubTaskResponse {
    private Long id;
    private Long taskId;
    private Integer status;
    private String description;
}
