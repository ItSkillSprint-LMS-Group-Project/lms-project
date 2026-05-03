package com.example.lmsproject.assessment.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerRequest {

        @NotNull(message = "Question id is required")
        private Long questionId;

        private Long selectedOptionId;

        private String textAnswer;
}