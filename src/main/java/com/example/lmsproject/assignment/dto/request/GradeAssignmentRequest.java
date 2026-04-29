package com.example.lmsproject.assignment.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GradeAssignmentRequest {

    @NotNull(message = "Grade is required")
    @Min(0)
    @Max(100)
    private Integer grade;

    private String feedback;
}