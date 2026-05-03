package com.example.lmsproject.assessment.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOptionRequest {

        @NotNull(message = "Question id is required")
        private Long questionId;

        @NotBlank(message = "Option text is required")
        @Size(max = 500, message = "Option text must be less than 500 characters")
        private String text;

        private boolean correct;
}