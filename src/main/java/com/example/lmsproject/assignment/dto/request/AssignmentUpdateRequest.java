package com.example.lmsproject.assignment.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignmentUpdateRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
}
