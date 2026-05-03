package com.example.lmsproject.assessment.repository;

import com.example.lmsproject.assessment.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByAssessmentId(Long assessmentId);
}