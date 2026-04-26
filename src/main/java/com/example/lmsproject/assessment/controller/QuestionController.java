package com.example.lmsproject.assessment.controller;

import com.example.lmsproject.assessment.dto.request.CreateQuestionRequest;
import com.example.lmsproject.assessment.dto.response.QuestionResponse;
import com.example.lmsproject.assessment.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody CreateQuestionRequest request) {
        return ResponseEntity.status(201).body(questionService.createQuestion(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByAssessment(@PathVariable Long assessmentId) {
        return ResponseEntity.ok(questionService.getQuestionsByAssessment(assessmentId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("Question deleted successfully");
    }
}