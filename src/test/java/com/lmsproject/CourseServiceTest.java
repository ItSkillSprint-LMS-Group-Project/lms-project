package com.lmsproject;

import com.example.lmsproject.course.dto.CourseRequest;
import com.example.lmsproject.course.dto.CourseResponse;
import com.example.lmsproject.course.dto.CourseUpdateRequest;
import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.course.mapper.CourseMapper;
import com.example.lmsproject.course.repository.CourseRepository;
import com.example.lmsproject.course.service.CourseService;
import com.example.lmsproject.enrollment.entity.Enrollment;
import com.example.lmsproject.enrollment.repository.EnrollmentRepository;
import com.example.lmsproject.enrollment.service.EnrollmentService;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.user.dto.response.UserResponse;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.mapper.UserMapper;
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
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    void createCourse_ShouldCreateSuccessfully() {

        CourseRequest request = new CourseRequest();

        User teacher = new User();
        teacher.setId(1L);

        Course course = new Course();
        course.setId(10L);

        CourseResponse response = new CourseResponse();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(teacher));

        when(courseRepository.save(any(Course.class)))
                .thenReturn(course);

        try (MockedStatic<CourseMapper> mapper = mockStatic(CourseMapper.class)) {

            mapper.when(() -> CourseMapper.toEntity(request))
                    .thenReturn(course);

            mapper.when(() -> CourseMapper.toResponse(course))
                    .thenReturn(response);

            CourseResponse result =
                    courseService.createCourse(request, 1L);

            assertNotNull(result);
            verify(courseRepository).save(any(Course.class));
        }
    }

    @Test
    void createCourse_ShouldThrow_WhenTeacherNotFound() {

        CourseRequest request = new CourseRequest();

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> courseService.createCourse(request, 1L));
    }

    @Test
    void getCourseById_ShouldReturnCourse() {

        Course course = new Course();
        course.setId(1L);

        CourseResponse response = new CourseResponse();

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        try (MockedStatic<CourseMapper> mapper = mockStatic(CourseMapper.class)) {

            mapper.when(() -> CourseMapper.toResponse(course))
                    .thenReturn(response);

            CourseResponse result = courseService.getCourseById(1L);

            assertNotNull(result);
        }
    }

    @Test
    void getCourseById_ShouldThrow_WhenNotFound() {

        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> courseService.getCourseById(1L));
    }

    @Test
    void updateCourse_ShouldUpdateSuccessfully() {

        CourseUpdateRequest request = new CourseUpdateRequest();

        Course course = new Course();
        course.setId(1L);

        CourseResponse response = new CourseResponse();

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(courseRepository.save(course))
                .thenReturn(course);

        try (MockedStatic<CourseMapper> mapper = mockStatic(CourseMapper.class)) {

            mapper.when(() -> CourseMapper.toResponse(course))
                    .thenReturn(response);

            CourseResponse result =
                    courseService.updateCourse(1L, request);

            assertNotNull(result);
            verify(courseRepository).save(course);
        }
    }

    @Test
    void deleteCourse_ShouldDeleteSuccessfully() {

        Course course = new Course();

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);

        verify(courseRepository).delete(course);
    }

    @Test
    void getStudentsByCourse_ShouldReturnUsers() {

        Course course = new Course();

        User student = new User();
        student.setId(10L);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);

        UserResponse userResponse = new UserResponse();

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(enrollmentRepository.findByCourseId(1L))
                .thenReturn(List.of(enrollment));

        when(userMapper.ToResponse(student))
                .thenReturn(userResponse);

        List<UserResponse> result =
                courseService.getStudentsByCourse(1L);

        assertEquals(1, result.size());
    }

    @Test
    void isOwner_ShouldReturnTrue() {

        when(courseRepository.existsByIdAndTeacherId(1L, 2L))
                .thenReturn(true);

        assertTrue(courseService.isOwner(1L, 2L));
    }

    @Test
    void canAccessCourse_ShouldReturnTrue_WhenEnrolled() {

        when(courseRepository.existsByIdAndTeacherId(1L, 2L))
                .thenReturn(false);

        when(enrollmentService.isEnrolled(1L, 2L))
                .thenReturn(true);

        assertTrue(courseService.canAccessCourse(1L, 2L));
    }
}