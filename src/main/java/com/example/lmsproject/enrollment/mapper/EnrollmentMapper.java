package com.example.lmsproject.enrollment.mapper;

import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.enrollment.dto.EnrollByCodeRequest;
import com.example.lmsproject.enrollment.dto.EnrollmentResponse;
import com.example.lmsproject.enrollment.entity.Enrollment;
import com.example.lmsproject.enrollment.entity.EnrollmentStatus;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

public class EnrollmentMapper {
    public static Enrollment toEntity(Course course, User student){
            Enrollment enrollment = new Enrollment();
            enrollment.setCourse(course);
            enrollment.setStudent(student);
            enrollment.setStatus(EnrollmentStatus.ACTIVE);
            return enrollment;
        }
    public static EnrollmentResponse toResponse(Enrollment enrollment){
        EnrollmentResponse response = new EnrollmentResponse();
        response.setId(enrollment.getId());
        response.setStatus(enrollment.getStatus().name());
        response.setEnrolledAt(enrollment.getEnrolledAt());
        response.setCourseId(enrollment.getCourse().getId());
        response.setCourseTitle(enrollment.getCourse().getTitle());
        response.setStudentId(enrollment.getStudent().getId());
        return response;
    }
    public static List<EnrollmentResponse> toResponseList(List<Enrollment> enrollments) {
        return enrollments.stream()
                .map(EnrollmentMapper::toResponse)
                .toList();
    }
    public static void updateStatus(Enrollment enrollment, EnrollmentStatus status) {
        enrollment.setStatus(status);
    }
}
