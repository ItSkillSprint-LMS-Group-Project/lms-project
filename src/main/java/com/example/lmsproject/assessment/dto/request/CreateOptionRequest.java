package com.example.lmsproject.assessment.dto.request;

import jakarta.validation.constraints.*;

public record CreateOptionRequest(

        @NotNull(message = "Question id is required")
        Long questionId,

        @NotBlank(message = "Option text is required")
        @Size(max = 500, message = "Option text must be less than 500 characters")
        String text,

        boolean correct
) {
}