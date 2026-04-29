package com.example.lmsproject.assignment.service;

import com.example.lmsproject.assignment.dto.request.AssignmentRequest;
import com.example.lmsproject.assignment.dto.request.AssignmentSubmissionRequest;
import com.example.lmsproject.assignment.dto.request.AssignmentUpdateRequest;
import com.example.lmsproject.assignment.dto.request.GradeAssignmentRequest;
import com.example.lmsproject.assignment.dto.response.AssignmentResponse;
import com.example.lmsproject.assignment.dto.response.AssignmentSubmissionResponse;
import com.example.lmsproject.assignment.entity.Assignment;
import com.example.lmsproject.assignment.entity.AssignmentSubmission;
import com.example.lmsproject.assignment.mapper.AssignmentMapper;
import com.example.lmsproject.assignment.repository.AssignmentRepository;
import com.example.lmsproject.assignment.repository.AssignmentSubmissionRepository;
import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.course.repository.CourseRepository;
import com.example.lmsproject.enrollment.repository.EnrollmentRepository;
import com.example.lmsproject.exception.AlreadyExistsException;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.exception.UnauthorizedException;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final UserRepository userRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository,
                             AssignmentSubmissionRepository assignmentSubmissionRepository,
                             UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.assignmentSubmissionRepository = assignmentSubmissionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AssignmentResponse createAssignment(AssignmentRequest request, Long teacherId) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Assignment assignment = AssignmentMapper.toEntity(request, course);
        assignmentRepository.save(assignment);
        return AssignmentMapper.toResponse(assignment);
    }
    public AssignmentResponse getAssignmentById(Long id) {
        Assignment assignment = findAssignment(id);
        return AssignmentMapper.toResponse(assignment);
    }
    public List<AssignmentResponse> getAssignmentsByCourse(Long courseId) {
        return AssignmentMapper.toResponseList(assignmentRepository.findByCourseId(courseId)
        );
    }

    public List<AssignmentResponse> getAssignmentsForStudent(Long studentId) {
        List<Long> courseIds = enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(e -> e.getCourse().getId())
                .toList();
        List<Assignment> assignments = assignmentRepository.findByCourseIdIn(courseIds);
        return AssignmentMapper.toResponseList(assignments);
    }
    @Transactional
    public AssignmentResponse updateAssignment(Long id, AssignmentUpdateRequest request) {
        Assignment assignment = findAssignment(id);
        AssignmentMapper.updateEntity(assignment, request);
        return AssignmentMapper.toResponse(assignment);
    }
    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = findAssignment(id);
        assignmentRepository.delete(assignment);
    }

    @Transactional
    public AssignmentSubmissionResponse submitAssignment(Long assignmentId, AssignmentSubmissionRequest request, Long studentId) {
        if(assignmentSubmissionRepository.existsByStudentId(studentId)) {
            throw new AlreadyExistsException("Already submitted");
        }
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        AssignmentSubmission submission =
                AssignmentMapper.toSubmissionEntity(request, assignment, student);

        assignmentSubmissionRepository.save(submission);

        return AssignmentMapper.toSubmissionResponse(submission);
    }

    @Transactional
    public AssignmentSubmissionResponse gradeAssignment(Long submissionId,
                                                        GradeAssignmentRequest request) {

        AssignmentSubmission submission = assignmentSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        submission.setGrade(request.getGrade());

        if (request.getFeedback() != null) {
            submission.setFeedback(request.getFeedback());
        }

        assignmentSubmissionRepository.save(submission);

        return AssignmentMapper.toSubmissionResponse(submission);
    }

    public boolean canAccess(Long assignmentId, Long userId) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElse(null);

        if (assignment == null) return false;

        Long courseId = assignment.getCourse().getId();

        return courseRepository.existsByIdAndTeacherId(courseId, userId)
                || enrollmentRepository.existsByCourseIdAndStudentId(courseId, userId);
    }

    public boolean canModify(Long assignmentId, Long userId) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElse(null);

        if (assignment == null) return false;

        return courseRepository.existsByIdAndTeacherId(
                assignment.getCourse().getId(), userId);
    }

    public boolean canCreateForCourse(Long courseId, Long userId) {
        return courseRepository.existsByIdAndTeacherId(courseId, userId);
    }

    private Assignment findAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
    }
    @Transactional
    public AssignmentSubmissionResponse updateSubmission(Long submissionId,
                                                         AssignmentSubmissionRequest request,
                                                         Long studentId) {

        AssignmentSubmission submission = assignmentSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        if (!submission.getStudent().getId().equals(studentId)) {
            throw new UnauthorizedException("You can only update your own submission");
        }

        if (submission.getGrade() != null) {
            throw new UnauthorizedException("Cannot edit graded submission");
        }

        submission.setSubmissionText((request.getSubmissionText()));

        assignmentSubmissionRepository.save(submission);

        return AssignmentMapper.toSubmissionResponse(submission);
    }
    public boolean canModifySubmission(Long submissionId, Long userId) {
        return assignmentSubmissionRepository.existsByIdAndStudentId(submissionId, userId);
    }
}