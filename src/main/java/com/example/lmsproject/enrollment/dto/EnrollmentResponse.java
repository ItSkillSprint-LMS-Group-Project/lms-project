package com.example.lmsproject.enrollment.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class EnrollmentResponse {
    private Long id;
    private String status;
    private LocalDateTime enrolledAt;
    private Long courseId;
    private String courseTitle;
    private Long studentId;
}