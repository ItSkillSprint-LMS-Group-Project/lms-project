package com.example.lmsproject.assessment.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SubmitAssessmentRequest(

        @NotNull(message = "Assessment id is required")
        Long assessmentId,

        @NotEmpty(message = "Answers cannot be empty")
        List<@Valid SubmitAnswerRequest> answers
) {
}