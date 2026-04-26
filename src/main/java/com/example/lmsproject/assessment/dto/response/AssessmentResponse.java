package com.example.lmsproject.assessment.dto.response;

import com.example.lmsproject.assessment.entity.AssessmentType;

public record AssessmentResponse(
        Long id,
        String title,
        AssessmentType type,
        Integer timeLimitMinutes,
        String externalLink,
        Long courseId
) {
}