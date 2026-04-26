package com.example.lmsproject.assessment.mapper;

import com.example.lmsproject.assessment.dto.response.AssessmentResponse;
import com.example.lmsproject.assessment.entity.Assessment;
import org.springframework.stereotype.Component;

@Component
public class AssessmentMapper {

    public AssessmentResponse toResponse(Assessment assessment) {
        return new AssessmentResponse(
                assessment.getId(),
                assessment.getTitle(),
                assessment.getType(),
                assessment.getTimeLimitMinutes(),
                assessment.getExternalLink(),
                assessment.getCourse().getId()
        );
    }
}