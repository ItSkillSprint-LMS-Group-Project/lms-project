package com.example.lmsproject.user.entity.repository;

import com.example.lmsproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByStudentIdNumber(String studentIdNumber);
    boolean existsByTeacherIdNumber(String teacherIdNumber);
}
