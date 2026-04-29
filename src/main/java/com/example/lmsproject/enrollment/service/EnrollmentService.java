package com.example.lmsproject.enrollment.service;

import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.course.repository.CourseRepository;
import com.example.lmsproject.enrollment.dto.EnrollByCodeRequest;
import com.example.lmsproject.enrollment.dto.EnrollByEmailRequest;
import com.example.lmsproject.enrollment.dto.EnrollmentResponse;
import com.example.lmsproject.enrollment.entity.Enrollment;
import com.example.lmsproject.enrollment.mapper.EnrollmentMapper;
import com.example.lmsproject.enrollment.repository.EnrollmentRepository;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }
    public EnrollmentResponse enrollByCourseCode(EnrollByCodeRequest request, Long studentId) {
        Course course = courseRepository.findByCourseCode(request.getCourseCode())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        if (enrollmentRepository.existsByCourseIdAndStudentId(course.getId(), studentId)) {
            throw new RuntimeException("Already enrolled");
        }
        Enrollment enrollment = EnrollmentMapper.toEntity(course, student);
        enrollmentRepository.save(enrollment);
        return EnrollmentMapper.toResponse(enrollment);
    }
    public EnrollmentResponse enrollByEmail(Long courseId,EnrollByEmailRequest request){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User student=userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        if (enrollmentRepository.existsByCourseIdAndStudentId(course.getId(), student.getId())) {
            throw new RuntimeException("Already enrolled");
        }
        Enrollment enrollment= EnrollmentMapper.toEntity(course,student);
        enrollmentRepository.save(enrollment);
        return EnrollmentMapper.toResponse(enrollment);
    }
    public List<EnrollmentResponse> enrollmentsByStudent(Long studentId){
        return EnrollmentMapper.toResponseList(enrollmentRepository.findByStudentId(studentId));
    }

    public boolean isEnrolled(Long courseId, Long userId){
        return enrollmentRepository.existsByCourseIdAndStudentId(courseId, userId);
    }
}
