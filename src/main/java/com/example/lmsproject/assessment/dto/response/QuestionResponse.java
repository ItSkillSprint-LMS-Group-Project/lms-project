package com.example.lmsproject.assessment.dto.response;

import com.example.lmsproject.assessment.entity.QuestionType;

import java.util.List;

public record QuestionResponse(
        Long id,
        String text,
        QuestionType type,
        Integer points,
        Long assessmentId,
        List<OptionResponse> options
) {
}