package com.example.lmsproject.assignment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignmentSubmissionRequest {

    @NotBlank(message = "Submission text is required")
    private String submissionText;
}