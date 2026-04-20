package com.example.lmsproject.course.repository;

import com.example.lmsproject.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository  extends JpaRepository<Course,Long> {

    boolean existsByIdAndTeacherId(Long courseId, Long userId);

    List<Course> findByTeacherId(Long teacherId);
}
