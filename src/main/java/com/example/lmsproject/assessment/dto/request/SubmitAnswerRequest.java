package com.example.lmsproject.assessment.dto.request;

import jakarta.validation.constraints.NotNull;

public record SubmitAnswerRequest(

        @NotNull(message = "Question id is required")
        Long questionId,

        Long selectedOptionId,

        String textAnswer
) {
}