package com.example.lmsproject.course.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseResponse {
    private Long id;
    private String title;
    private Long teacherId;
    private String teacherFirstName;
    private String teacherLastName;
    private String description;
    private LocalDateTime createdAt;
    private String courseCode;
}
