package com.example.lmsproject.user.repository;

import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByStudentIdNumber(String studentIdNumber);
    boolean existsByTeacherIdNumber(String teacherIdNumber);
    List<User> findByRole(UserRole role);
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
