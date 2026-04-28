package com.example.lmsproject.assessment.dto.request;

import lombok.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAssessmentRequest {

        @NotNull(message = "Assessment id is required")
        private Long assessmentId;

        @NotEmpty(message = "Answers cannot be empty")
        private List<@Valid SubmitAnswerRequest> answers;
}