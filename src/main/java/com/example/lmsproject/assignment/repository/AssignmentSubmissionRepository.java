package com.example.lmsproject.assignment.repository;

import com.example.lmsproject.assignment.entity.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission,Long> {
    boolean existsByStudentId(Long studentId);

    boolean existsByIdAndStudentId(Long submissionId, Long userId);
}