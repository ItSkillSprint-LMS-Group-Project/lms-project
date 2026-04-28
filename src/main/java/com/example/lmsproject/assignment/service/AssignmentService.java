package com.example.lmsproject.assignment.service;

import com.example.lmsproject.assignment.entity.Assignment;
import com.example.lmsproject.assignment.repository.AssignmentRepository;
import com.example.lmsproject.assignment.dto.request.AssignmentRequest;
import com.example.lmsproject.assignment.dto.response.AssignmentResponse;
import com.example.lmsproject.assignment.dto.request.AssignmentUpdateRequest;
import com.example.lmsproject.assignment.mapper.AssignmentMapper;
import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.course.repository.CourseRepository;
import com.example.lmsproject.enrollment.repository.EnrollmentRepository;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public AssignmentResponse createAssignment(AssignmentRequest request, Long teacherId) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!course.getTeacher().getId().equals(teacherId)) {
            throw new UnauthorizedException("Unauthorized");
        }

        Assignment assignment = AssignmentMapper.toEntity(request, course);
        return AssignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    public AssignmentResponse getAssignmentById(Long id) {
        Assignment assignment = findAssignment(id);
        return AssignmentMapper.toResponse(assignment);
    }

    public List<AssignmentResponse> getAssignmentsByCourse(Long courseId) {
        return AssignmentMapper.toResponseList(assignmentRepository.findByCourseId(courseId));
    }

    public List<AssignmentResponse> getAssignmentsForStudent(Long studentId) {
        List<Long> enrolledCourseIds = enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(enrollment -> enrollment.getCourse().getId())
                .toList();

        List<Assignment> assignments = enrolledCourseIds.stream()
                .flatMap(courseId -> assignmentRepository.findByCourseId(courseId).stream())
                .toList();

        return AssignmentMapper.toResponseList(assignments);
    }

    @Transactional
    public AssignmentResponse updateAssignment(Long id, AssignmentUpdateRequest request) {
        Assignment assignment = findAssignment(id);
        AssignmentMapper.updateEntity(assignment, request);
        return AssignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = findAssignment(id);
        assignmentRepository.delete(assignment);
    }

    private Assignment findAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
    }

    public boolean canAccess(Long assignmentId, Long userId) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (assignment == null) return false;

        Long courseId = assignment.getCourse().getId();
        return courseRepository.existsByIdAndTeacherId(courseId, userId) ||
                enrollmentRepository.existsByCourseIdAndStudentId(courseId, userId);
    }

    public boolean canModify(Long assignmentId, Long userId) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (assignment == null) return false;
        return courseRepository.existsByIdAndTeacherId(assignment.getCourse().getId(), userId);
    }
    public boolean canCreateForCourse(Long courseId, Long userId) {
        return courseRepository.existsByIdAndTeacherId(courseId, userId);
    }
}