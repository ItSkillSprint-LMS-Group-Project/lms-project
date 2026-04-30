package com.lmsproject;

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
import com.example.lmsproject.assignment.service.AssignmentService;
import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.course.repository.CourseRepository;
import com.example.lmsproject.enrollment.entity.Enrollment;
import com.example.lmsproject.enrollment.repository.EnrollmentRepository;
import com.example.lmsproject.exception.AlreadyExistsException;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.exception.UnauthorizedException;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssignmentService assignmentService;

    @Test
    void createAssignment_ShouldCreateSuccessfully() {

        AssignmentRequest request = new AssignmentRequest();
        request.setCourseId(1L);

        Course course = new Course();
        course.setId(1L);

        Assignment assignment = new Assignment();
        AssignmentResponse response = new AssignmentResponse();

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(assignmentRepository.save(any(Assignment.class)))
                .thenReturn(assignment);

        try (MockedStatic<AssignmentMapper> mapper = mockStatic(AssignmentMapper.class)) {

            mapper.when(() -> AssignmentMapper.toEntity(request, course))
                    .thenReturn(assignment);

            mapper.when(() -> AssignmentMapper.toResponse(assignment))
                    .thenReturn(response);

            AssignmentResponse result =
                    assignmentService.createAssignment(request, 1L);

            assertNotNull(result);
            verify(assignmentRepository).save(assignment);
        }
    }

    @Test
    void createAssignment_ShouldThrow_WhenCourseNotFound() {

        AssignmentRequest request = new AssignmentRequest();
        request.setCourseId(1L);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.createAssignment(request, 1L));
    }

    @Test
    void getAssignmentById_ShouldReturnAssignment() {

        Assignment assignment = new Assignment();
        AssignmentResponse response = new AssignmentResponse();

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.of(assignment));

        try (MockedStatic<AssignmentMapper> mapper = mockStatic(AssignmentMapper.class)) {

            mapper.when(() -> AssignmentMapper.toResponse(assignment))
                    .thenReturn(response);

            AssignmentResponse result =
                    assignmentService.getAssignmentById(1L);

            assertNotNull(result);
        }
    }

    @Test
    void getAssignmentById_ShouldThrow_WhenNotFound() {

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.getAssignmentById(1L));
    }

    @Test
    void submitAssignment_ShouldSubmitSuccessfully() {

        AssignmentSubmissionRequest request = new AssignmentSubmissionRequest();

        Assignment assignment = new Assignment();
        User student = new User();

        AssignmentSubmission submission = new AssignmentSubmission();
        AssignmentSubmissionResponse response = new AssignmentSubmissionResponse();

        when(assignmentSubmissionRepository.existsByStudentId(1L))
                .thenReturn(false);

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.of(assignment));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(assignmentSubmissionRepository.save(any()))
                .thenReturn(submission);

        try (MockedStatic<AssignmentMapper> mapper = mockStatic(AssignmentMapper.class)) {

            mapper.when(() -> AssignmentMapper.toSubmissionEntity(request, assignment, student))
                    .thenReturn(submission);

            mapper.when(() -> AssignmentMapper.toSubmissionResponse(submission))
                    .thenReturn(response);

            AssignmentSubmissionResponse result =
                    assignmentService.submitAssignment(1L, request, 1L);

            assertNotNull(result);
        }
    }

    @Test
    void submitAssignment_ShouldThrow_WhenAlreadySubmitted() {

        when(assignmentSubmissionRepository.existsByStudentId(1L))
                .thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> assignmentService.submitAssignment(1L, new AssignmentSubmissionRequest(), 1L));
    }

    @Test
    void gradeAssignment_ShouldGradeSuccessfully() {

        GradeAssignmentRequest request = new GradeAssignmentRequest();
        request.setGrade(90);

        AssignmentSubmission submission = new AssignmentSubmission();
        AssignmentSubmissionResponse response = new AssignmentSubmissionResponse();

        when(assignmentSubmissionRepository.findById(1L))
                .thenReturn(Optional.of(submission));

        when(assignmentSubmissionRepository.save(submission))
                .thenReturn(submission);

        try (MockedStatic<AssignmentMapper> mapper = mockStatic(AssignmentMapper.class)) {

            mapper.when(() -> AssignmentMapper.toSubmissionResponse(submission))
                    .thenReturn(response);

            AssignmentSubmissionResponse result =
                    assignmentService.gradeAssignment(1L, request);

            assertNotNull(result);
            verify(assignmentSubmissionRepository).save(submission);
        }
    }

    @Test
    void canAccess_ShouldReturnTrue_WhenTeacher() {

        Assignment assignment = new Assignment();
        Course course = new Course();
        course.setId(1L);
        assignment.setCourse(course);

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.of(assignment));

        when(courseRepository.existsByIdAndTeacherId(1L, 2L))
                .thenReturn(true);

        assertTrue(assignmentService.canAccess(1L, 2L));
    }

    @Test
    void canModify_ShouldReturnTrue_WhenTeacher() {

        Assignment assignment = new Assignment();
        Course course = new Course();
        course.setId(1L);
        assignment.setCourse(course);

        when(assignmentRepository.findById(1L))
                .thenReturn(Optional.of(assignment));

        when(courseRepository.existsByIdAndTeacherId(1L, 2L))
                .thenReturn(true);

        assertTrue(assignmentService.canModify(1L, 2L));
    }

    @Test
    void updateSubmission_ShouldThrow_WhenNotOwner() {

        AssignmentSubmission submission = new AssignmentSubmission();
        User student = new User();
        student.setId(99L);
        submission.setStudent(student);

        when(assignmentSubmissionRepository.findById(1L))
                .thenReturn(Optional.of(submission));

        assertThrows(UnauthorizedException.class,
                () -> assignmentService.updateSubmission(1L, new AssignmentSubmissionRequest(), 1L));
    }

    @Test
    void canModifySubmission_ShouldReturnTrue() {

        when(assignmentSubmissionRepository.existsByIdAndStudentId(1L, 1L))
                .thenReturn(true);

        assertTrue(assignmentService.canModifySubmission(1L, 1L));
    }
}