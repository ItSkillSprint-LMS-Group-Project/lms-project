package com.lmsproject;

import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.course.repository.CourseRepository;
import com.example.lmsproject.enrollment.dto.EnrollByCodeRequest;
import com.example.lmsproject.enrollment.dto.EnrollByEmailRequest;
import com.example.lmsproject.enrollment.dto.EnrollmentResponse;
import com.example.lmsproject.enrollment.entity.Enrollment;
import com.example.lmsproject.enrollment.mapper.EnrollmentMapper;
import com.example.lmsproject.enrollment.repository.EnrollmentRepository;
import com.example.lmsproject.enrollment.service.EnrollmentService;
import com.example.lmsproject.exception.AlreadyExistsException;
import com.example.lmsproject.exception.ResourceNotFoundException;
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
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    void enrollByCourseCode_ShouldEnrollSuccessfully() {

        EnrollByCodeRequest request = new EnrollByCodeRequest();
        request.setCourseCode("JAVA101");

        Course course = new Course();
        course.setId(1L);

        User student = new User();
        student.setId(10L);

        Enrollment enrollment = new Enrollment();

        EnrollmentResponse response = new EnrollmentResponse();

        when(courseRepository.findByCourseCode("JAVA101"))
                .thenReturn(Optional.of(course));

        when(userRepository.findById(10L))
                .thenReturn(Optional.of(student));

        when(enrollmentRepository.existsByCourseIdAndStudentId(1L, 10L))
                .thenReturn(false);

        try (MockedStatic<EnrollmentMapper> mapper = mockStatic(EnrollmentMapper.class)) {

            mapper.when(() -> EnrollmentMapper.toEntity(course, student))
                    .thenReturn(enrollment);

            mapper.when(() -> EnrollmentMapper.toResponse(enrollment))
                    .thenReturn(response);

            EnrollmentResponse result =
                    enrollmentService.enrollByCourseCode(request, 10L);

            assertNotNull(result);
            verify(enrollmentRepository).save(enrollment);
        }
    }

    @Test
    void enrollByCourseCode_ShouldThrow_WhenCourseNotFound() {

        EnrollByCodeRequest request = new EnrollByCodeRequest();
        request.setCourseCode("JAVA101");

        when(courseRepository.findByCourseCode(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> enrollmentService.enrollByCourseCode(request, 1L));
    }

    @Test
    void enrollByCourseCode_ShouldThrow_WhenAlreadyEnrolled() {

        EnrollByCodeRequest request = new EnrollByCodeRequest();
        request.setCourseCode("JAVA101");

        Course course = new Course();
        course.setId(1L);

        User student = new User();
        student.setId(10L);

        when(courseRepository.findByCourseCode(anyString()))
                .thenReturn(Optional.of(course));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(student));

        when(enrollmentRepository.existsByCourseIdAndStudentId(1L, 10L))
                .thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> enrollmentService.enrollByCourseCode(request, 10L));
    }

    @Test
    void enrollByEmail_ShouldEnrollSuccessfully() {

        EnrollByEmailRequest request = new EnrollByEmailRequest();
        request.setEmail("test@gmail.com");

        Course course = new Course();
        course.setId(1L);

        User student = new User();
        student.setId(10L);

        Enrollment enrollment = new Enrollment();
        EnrollmentResponse response = new EnrollmentResponse();

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(student));

        when(enrollmentRepository.existsByCourseIdAndStudentId(1L, 10L))
                .thenReturn(false);

        try (MockedStatic<EnrollmentMapper> mapper = mockStatic(EnrollmentMapper.class)) {

            mapper.when(() -> EnrollmentMapper.toEntity(course, student))
                    .thenReturn(enrollment);

            mapper.when(() -> EnrollmentMapper.toResponse(enrollment))
                    .thenReturn(response);

            EnrollmentResponse result =
                    enrollmentService.enrollByEmail(1L, request);

            assertNotNull(result);
            verify(enrollmentRepository).save(enrollment);
        }
    }

    @Test
    void enrollByEmail_ShouldThrow_WhenStudentNotFound() {

        EnrollByEmailRequest request = new EnrollByEmailRequest();
        request.setEmail("test@gmail.com");

        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> enrollmentService.enrollByEmail(1L, request));
    }

    @Test
    void isEnrolled_ShouldReturnTrue() {

        when(enrollmentRepository.existsByCourseIdAndStudentId(1L, 10L))
                .thenReturn(true);

        boolean result = enrollmentService.isEnrolled(1L, 10L);

        assertTrue(result);
    }

    @Test
    void enrollmentsByStudent_ShouldReturnList() {

        Enrollment enrollment = new Enrollment();

        EnrollmentResponse response = new EnrollmentResponse();

        when(enrollmentRepository.findByStudentId(10L))
                .thenReturn(List.of(enrollment));

        try (MockedStatic<EnrollmentMapper> mapper = mockStatic(EnrollmentMapper.class)) {

            mapper.when(() -> EnrollmentMapper.toResponseList(List.of(enrollment)))
                    .thenReturn(List.of(response));

            List<EnrollmentResponse> result =
                    enrollmentService.enrollmentsByStudent(10L);

            assertEquals(1, result.size());
        }
    }
}