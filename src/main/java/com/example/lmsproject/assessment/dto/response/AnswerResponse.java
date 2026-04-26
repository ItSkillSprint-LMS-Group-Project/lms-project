package com.example.lmsproject.assessment.dto.response;

public record AnswerResponse(
        Long id,
        Long questionId,
        Long selectedOptionId,
        String textAnswer
) {
}