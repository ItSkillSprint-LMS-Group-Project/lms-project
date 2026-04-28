package com.example.lmsproject.assessment.dto.request;

import com.example.lmsproject.assessment.entity.AssessmentType;
import jakarta.validation.constraints.*;

public record UpdateAssessmentRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title must be less than 150 characters")
        String title,

        @NotNull(message = "Assessment type is required")
        AssessmentType type,

        @NotNull(message = "Time limit is required")
        @Min(value = 1, message = "Time limit must be at least 1 minute")
        Integer timeLimitMinutes,

        @Size(max = 500, message = "External link must be less than 500 characters")
        String externalLink
) {
}