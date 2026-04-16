package com.example.lmsproject.assessmentSubmission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentSubmissionRepository extends JpaRepository<AssessmentSubmission,Long> {
}
