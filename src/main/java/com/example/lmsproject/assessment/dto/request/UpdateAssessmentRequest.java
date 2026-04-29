package com.example.lmsproject.assessment.dto.request;

import com.example.lmsproject.assessment.entity.AssessmentType;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAssessmentRequest {

        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title must be less than 150 characters")
        private String title;

        @NotNull(message = "Assessment type is required")
        private AssessmentType assessmentType;

        @NotNull(message = "Time limit is required")
        @Min(value = 1, message = "Time limit must be at least 1 minute")
        private Integer timeLimitMinutes;

        @Size(max = 500, message = "External link must be less than 500 characters")
        private String externalLink;
}