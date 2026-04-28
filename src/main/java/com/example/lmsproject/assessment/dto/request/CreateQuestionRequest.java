package com.example.lmsproject.assessment.dto.request;

import com.example.lmsproject.assessment.entity.QuestionType;
import jakarta.validation.constraints.*;

public record CreateQuestionRequest(

        @NotNull(message = "Assessment id is required")
        Long assessmentId,

        @NotBlank(message = "Question text is required")
        @Size(max = 1000, message = "Question text must be less than 1000 characters")
        String text,

        @NotNull(message = "Question type is required")
        QuestionType type,

        @NotNull(message = "Points is required")
        @Min(value = 1, message = "Points must be at least 1")
        Integer points
) {
}