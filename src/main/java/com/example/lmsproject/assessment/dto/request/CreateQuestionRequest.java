package com.example.lmsproject.assessment.dto.request;

import com.example.lmsproject.assessment.entity.QuestionType;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequest {

        @NotNull(message = "Assessment id is required")
        private Long assessmentId;

        @NotBlank(message = "Question text is required")
        @Size(max = 1000, message = "Question text must be less than 1000 characters")
        private String text;

        @NotNull(message = "Question type is required")
        private QuestionType type;

        @NotNull(message = "Points is required")
        @Min(value = 1, message = "Points must be at least 1")
        private Integer points;
}