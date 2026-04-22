package com.example.lmsproject.course.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.bridge.IMessage;

@Data
public class CourseRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 50,message = "Title must be between 2 and 50 characters")
    private String title;

    private String description;
}
