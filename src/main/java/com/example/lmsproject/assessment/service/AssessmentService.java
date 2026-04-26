package com.example.lmsproject.assessment.service;

import com.example.lmsproject.assessment.dto.request.CreateAssessmentRequest;
import com.example.lmsproject.assessment.dto.request.UpdateAssessmentRequest;
import com.example.lmsproject.assessment.dto.response.AssessmentResponse;
import com.example.lmsproject.assessment.entity.Assessment;
import com.example.lmsproject.assessment.mapper.AssessmentMapper;
import com.example.lmsproject.assessment.repository.AssessmentRepository;
import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.course.repository.CourseRepository;
import com.example.lmsproject.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final CourseRepository courseRepository;
    private final AssessmentMapper assessmentMapper;

    public AssessmentResponse createAssessment(CreateAssessmentRequest request) {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Assessment assessment = new Assessment();
        assessment.setTitle(request.title());
        assessment.setType(request.type());
        assessment.setTimeLimitMinutes(request.timeLimitMinutes());
        assessment.setExternalLink(request.externalLink());
        assessment.setCourse(course);

        return assessmentMapper.toResponse(assessmentRepository.save(assessment));
    }

    public List<AssessmentResponse> getAllAssessments() {
        return assessmentRepository.findAll()
                .stream()
                .map(assessmentMapper::toResponse)
                .toList();
    }

    public AssessmentResponse getAssessmentById(Long id) {
        Assessment assessment = findAssessmentById(id);
        return assessmentMapper.toResponse(assessment);
    }

    public List<AssessmentResponse> getAssessmentsByCourse(Long courseId) {
        return assessmentRepository.findByCourseId(courseId)
                .stream()
                .map(assessmentMapper::toResponse)
                .toList();
    }

    public AssessmentResponse updateAssessment(Long id, UpdateAssessmentRequest request) {
        Assessment assessment = findAssessmentById(id);

        assessment.setTitle(request.title());
        assessment.setType(request.type());
        assessment.setTimeLimitMinutes(request.timeLimitMinutes());
        assessment.setExternalLink(request.externalLink());

        return assessmentMapper.toResponse(assessmentRepository.save(assessment));
    }

    public void deleteAssessment(Long id) {
        Assessment assessment = findAssessmentById(id);
        assessmentRepository.delete(assessment);
    }

    public Assessment findAssessmentById(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));
    }
}