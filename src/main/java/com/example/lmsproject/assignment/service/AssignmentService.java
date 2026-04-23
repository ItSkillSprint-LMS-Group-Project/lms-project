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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class   AssignmentService {

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

    public AssignmentResponse getAssignmentById(Long id, Long userId, Collection<? extends GrantedAuthority> authorities) {
        Assignment assignment = findAssignment(id);

        boolean isTeacher = authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));
        boolean isStudent = authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"));

        if (isTeacher) {
            if (!assignment.getCourse().getTeacher().getId().equals(userId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        } else if (isStudent) {
            if (!enrollmentRepository.existsByCourseIdAndStudentId(assignment.getCourse().getId(), userId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        }

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
    public AssignmentResponse updateAssignment(Long id, AssignmentUpdateRequest request, Long teacherId) {
        Assignment assignment = findAssignment(id);

        if (!assignment.getCourse().getTeacher().getId().equals(teacherId)) {
            throw new UnauthorizedException("Unauthorized");
        }

        AssignmentMapper.updateEntity(assignment, request);
        return AssignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Transactional
    public void deleteAssignment(Long id, Long teacherId) {
        Assignment assignment = findAssignment(id);

        if (!assignment.getCourse().getTeacher().getId().equals(teacherId)) {
            throw new UnauthorizedException("Unauthorized");
        }

        assignmentRepository.delete(assignment);
    }

    private Assignment findAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
    }
}
