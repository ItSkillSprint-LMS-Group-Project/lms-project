package com.example.lmsproject.assignment.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignmentResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Long courseId;
    private String courseTitle;
}
