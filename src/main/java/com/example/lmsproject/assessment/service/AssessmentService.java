package com.example.lmsproject.assessment.service;

import com.example.lmsproject.assessment.dto.request.CreateAssessmentRequest;
import com.example.lmsproject.assessment.dto.request.UpdateAssessmentRequest;
import com.example.lmsproject.assessment.dto.response.AssessmentResponse;
import com.example.lmsproject.assessment.entity.Assessment;
import com.example.lmsproject.assessment.mapper.AssessmentMapper;
import com.example.lmsproject.assessment.repository.AssessmentRepository;
import com.example.lmsproject.assessment.repository.AssessmentSubmissionRepository;
import com.example.lmsproject.assessment.repository.OptionRepository;
import com.example.lmsproject.assessment.repository.QuestionRepository;
import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.course.repository.CourseRepository;
import com.example.lmsproject.enrollment.repository.EnrollmentRepository;
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
    private final EnrollmentRepository enrollmentRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final AssessmentSubmissionRepository submissionRepository;

    public AssessmentResponse createAssessment(CreateAssessmentRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Assessment assessment=AssessmentMapper.toEntity(request,course);

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
        assessmentMapper.updateEntity(assessment,request);

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

    public boolean canAccess(Long assessmentId, Long userId) {
        Assessment assessment = assessmentRepository.findById(assessmentId).orElse(null);

        if (assessment == null) {
            return false;
        }

        Long courseId = assessment.getCourse().getId();

        return courseRepository.existsByIdAndTeacherId(courseId, userId)
                || enrollmentRepository.existsByCourseIdAndStudentId(courseId, userId);
    }

    public boolean canModify(Long assessmentId, Long userId) {
        Assessment assessment = assessmentRepository.findById(assessmentId).orElse(null);

        if (assessment == null) {
            return false;
        }

        return courseRepository.existsByIdAndTeacherId(assessment.getCourse().getId(), userId);
    }

    public boolean canCreateForCourse(Long courseId, Long userId) {
        return courseRepository.existsByIdAndTeacherId(courseId, userId);
    }

    public boolean canAccessCourseAssessments(Long courseId, Long userId) {
        return courseRepository.existsByIdAndTeacherId(courseId, userId)
                || enrollmentRepository.existsByCourseIdAndStudentId(courseId, userId);
    }

    public boolean canModifyQuestion(Long questionId, Long userId) {
        return questionRepository.findById(questionId)
                .map(question -> canModify(question.getAssessment().getId(), userId))
                .orElse(false);
    }

    public boolean canModifyOption(Long optionId, Long userId) {
        return optionRepository.findById(optionId)
                .map(option -> canModifyQuestion(option.getQuestion().getId(), userId))
                .orElse(false);
    }

    public boolean canAccessSubmission(Long submissionId, Long userId) {
        return submissionRepository.findById(submissionId)
                .map(submission ->
                        submission.getStudent().getId().equals(userId)
                                || canModify(submission.getAssessment().getId(), userId)
                )
                .orElse(false);
    }
}