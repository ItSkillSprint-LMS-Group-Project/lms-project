package com.example.lmsproject.assessment.repository;

import com.example.lmsproject.assessment.entity.AssessmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentSubmissionRepository extends JpaRepository<AssessmentSubmission, Long> {

    boolean existsByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    Optional<AssessmentSubmission> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    List<AssessmentSubmission> findByAssessmentId(Long assessmentId);

    List<AssessmentSubmission> findByStudentId(Long studentId);
}