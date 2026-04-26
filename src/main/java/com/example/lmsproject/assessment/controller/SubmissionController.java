package com.example.lmsproject.assessment.controller;

import com.example.lmsproject.assessment.dto.request.SubmitAssessmentRequest;
import com.example.lmsproject.assessment.dto.response.AssessmentSubmissionResponse;
import com.example.lmsproject.assessment.service.SubmissionService;
import com.example.lmsproject.security.model.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<AssessmentSubmissionResponse> submitAssessment(
            @Valid @RequestBody SubmitAssessmentRequest request,
            Authentication authentication) {

        Long studentId = getCurrentUserId(authentication);
        return ResponseEntity.status(201).body(submissionService.submitAssessment(studentId, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<List<AssessmentSubmissionResponse>> getSubmissionsByAssessment(@PathVariable Long assessmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByAssessment(assessmentId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AssessmentSubmissionResponse>> getSubmissionsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByStudent(studentId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentSubmissionResponse> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }

    private Long getCurrentUserId(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }
}