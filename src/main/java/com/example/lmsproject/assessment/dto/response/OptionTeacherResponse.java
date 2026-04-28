package com.example.lmsproject.assessment.dto.response;

public record OptionTeacherResponse(
        Long id,
        String text,
        boolean correct
) {
}