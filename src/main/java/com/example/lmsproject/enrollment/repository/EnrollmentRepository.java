package com.example.lmsproject.enrollment.repository;

import com.example.lmsproject.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId );
    List<Enrollment> findByStudentId( Long studentId);
    List<Enrollment> findByCourseTeacherId(Long teacherId);
}
