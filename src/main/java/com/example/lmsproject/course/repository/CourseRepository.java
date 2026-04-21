package com.example.lmsproject.course.repository;

import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository  extends JpaRepository<Course,Long> {

    boolean existsByIdAndTeacherId(Long courseId, Long userId);

    List<Course> findByTeacherId(Long teacherId);

    Optional<Course> findByCourseCode(String courseCode);

}
