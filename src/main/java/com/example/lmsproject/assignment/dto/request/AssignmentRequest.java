package com.example.lmsproject.assignment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignmentRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    private Integer orderIndex;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}
