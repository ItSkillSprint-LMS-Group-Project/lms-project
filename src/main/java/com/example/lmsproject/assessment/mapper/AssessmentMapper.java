package com.example.lmsproject.assessment.mapper;

import com.example.lmsproject.assessment.dto.request.CreateAssessmentRequest;
import com.example.lmsproject.assessment.dto.request.UpdateAssessmentRequest;
import com.example.lmsproject.assessment.dto.response.AssessmentResponse;
import com.example.lmsproject.assessment.entity.Assessment;

import com.example.lmsproject.course.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class AssessmentMapper {
    public static Assessment toEntity(CreateAssessmentRequest request, Course course) {
        Assessment assessment = new Assessment();
        assessment.setTitle(request.getTitle());
        assessment.setType(request.getType());
        assessment.setTimeLimitMinutes(request.getTimeLimitMinutes());
        assessment.setExternalLink(request.getExternalLink());
        assessment.setCourse(course);
        return assessment;
    }

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
    public void updateEntity(Assessment assessment, UpdateAssessmentRequest request) {

        if (request.getTitle() != null) {
            assessment.setTitle(request.getTitle());
        }

        if (request.getType() != null) {
            assessment.setType(request.getType());
        }

        if (request.getTimeLimitMinutes() != null) {
            assessment.setTimeLimitMinutes(request.getTimeLimitMinutes());
        }

        if (request.getExternalLink() != null) {
            assessment.setExternalLink(request.getExternalLink());
        }
    }
}