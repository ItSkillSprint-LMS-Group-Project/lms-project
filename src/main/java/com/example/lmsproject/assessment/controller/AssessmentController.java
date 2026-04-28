package com.example.lmsproject.assessment.controller;

import com.example.lmsproject.assessment.dto.request.CreateAssessmentRequest;
import com.example.lmsproject.assessment.dto.request.UpdateAssessmentRequest;
import com.example.lmsproject.assessment.dto.response.AssessmentResponse;
import com.example.lmsproject.assessment.service.AssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AssessmentResponse>> getAllAssessments() {
        return ResponseEntity.ok(assessmentService.getAllAssessments());
    }

    @PreAuthorize("hasRole('ADMIN') or @assessmentService.canAccess(#id, authentication.principal.id)")
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentResponse> getAssessmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getAssessmentById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or @assessmentService.canAccessCourseAssessments(#courseId, authentication.principal.id)")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AssessmentResponse>> getAssessmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(assessmentService.getAssessmentsByCourse(courseId));
    }

    @PreAuthorize("hasRole('ADMIN') or @assessmentService.canCreateForCourse(#request.courseId(), authentication.principal.id)")
    @PostMapping
    public ResponseEntity<AssessmentResponse> createAssessment(@Valid @RequestBody CreateAssessmentRequest request) {
        return ResponseEntity.status(201).body(assessmentService.createAssessment(request));
    }

    @PreAuthorize("hasRole('ADMIN') or @assessmentService.canModify(#id, authentication.principal.id)")
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentResponse> updateAssessment(@PathVariable Long id,
                                                               @Valid @RequestBody UpdateAssessmentRequest request) {
        return ResponseEntity.ok(assessmentService.updateAssessment(id, request));
    }

    @PreAuthorize("hasRole('ADMIN') or @assessmentService.canModify(#id, authentication.principal.id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
        return ResponseEntity.ok("Assessment deleted successfully");
    }
}