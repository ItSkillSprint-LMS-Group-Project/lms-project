package com.example.lmsproject.course.service;

import com.example.lmsproject.course.dto.CourseRequest;
import com.example.lmsproject.course.dto.CourseResponse;
import com.example.lmsproject.course.dto.CourseUpdateRequest;
import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.course.mapper.CourseMapper;
import com.example.lmsproject.course.repository.CourseRepository;
import com.example.lmsproject.enrollment.service.EnrollmentService;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentService enrollmentService;

    public CourseService(CourseRepository courseRepository,
                         UserRepository userRepository, EnrollmentService enrollmentService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentService = enrollmentService;
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest request, Long teacherId) {

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        Course course = CourseMapper.toEntity(request);
        course.setTeacher(teacher);
        course.setCourseCode(generateCode());

        return CourseMapper.toResponse(courseRepository.save(course));
    }

    public CourseResponse getCourseById(Long id) {
        Course course = findCourse(id);
        return CourseMapper.toResponse(course);
    }

    public List<CourseResponse> getCoursesByTeacher(Long teacherId) {
        return CourseMapper.toResponseList(
                courseRepository.findByTeacherId(teacherId)
        );
    }

    public List<CourseResponse> getAllCourses() {
        return CourseMapper.toResponseList(courseRepository.findAll());
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseUpdateRequest request) {

        Course course = findCourse(id);

        CourseMapper.updateEntity(course, request);

        return CourseMapper.toResponse(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        Course course = findCourse(id);
        courseRepository.delete(course);
    }

    public boolean isOwner(Long courseId, Long userId) {
        return courseRepository.existsByIdAndTeacherId(courseId, userId);
    }

    public boolean canAccessCourse(Long courseId, Long userId) {
        return isOwner(courseId, userId)
                || enrollmentService.isEnrolled(courseId, userId);
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}