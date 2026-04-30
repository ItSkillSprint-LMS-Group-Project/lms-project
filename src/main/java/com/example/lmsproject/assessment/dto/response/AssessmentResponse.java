package com.example.lmsproject.assessment.dto.response;

import com.example.lmsproject.assessment.entity.AssessmentType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResponse {

    private Long id;
    private String title;
    private AssessmentType assessmentType;
    private Integer timeLimitMinutes;
    private String externalLink;
    private Long courseId;
}