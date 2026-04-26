package com.example.lmsproject.assessment.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record AssessmentSubmissionResponse(
        Long id,
        Long assessmentId,
        Long studentId,
        Integer totalScore,
        LocalDateTime submittedAt,
        List<AnswerResponse> answers
) {
}