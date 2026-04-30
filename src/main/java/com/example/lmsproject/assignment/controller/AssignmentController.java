package com.example.lmsproject.assignment.controller;

import com.example.lmsproject.assignment.dto.request.AssignmentRequest;
import com.example.lmsproject.assignment.dto.request.AssignmentSubmissionRequest;
import com.example.lmsproject.assignment.dto.request.GradeAssignmentRequest;
import com.example.lmsproject.assignment.dto.response.AssignmentResponse;
import com.example.lmsproject.assignment.dto.request.AssignmentUpdateRequest;
import com.example.lmsproject.assignment.dto.response.AssignmentSubmissionResponse;
import com.example.lmsproject.assignment.service.AssignmentService;
import com.example.lmsproject.security.model.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @PreAuthorize("@assignmentService.canCreateForCourse(#request.getCourseId(), authentication.principal.id) or hasRole('ADMIN')")
    public ResponseEntity<AssignmentResponse> createAssignment(@RequestBody @Valid AssignmentRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.createAssignment(request, user.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@assignmentService.canAccess(#id, authentication.principal.id)")
    public ResponseEntity<AssignmentResponse> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("@courseService.isOwner(#courseId, authentication.principal.id) or hasRole('ADMIN')")
    public ResponseEntity<List<AssignmentResponse>> getAssignmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByCourse(courseId));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AssignmentResponse>> getAssignmentsForStudent(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(assignmentService.getAssignmentsForStudent(user.getId()));
    }
    @PostMapping("/{id}/submit")
    @PreAuthorize("@assignmentService.canAccess(#id,authentication.principal.id)")
    public ResponseEntity<AssignmentSubmissionResponse> submitAssignment(@PathVariable Long id, @RequestBody @Valid AssignmentSubmissionRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.submitAssignment(id, request,user.getId()));
    }
    @PatchMapping("/{id}/submissions/{submissionId}/grade")
    @PreAuthorize("@assignmentService.canModify(#id, authentication.principal.id)")
    public ResponseEntity<AssignmentSubmissionResponse> gradeAssignment(@PathVariable Long id,@PathVariable Long submissionId, @RequestBody @Valid GradeAssignmentRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(assignmentService.gradeAssignment(submissionId, request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@assignmentService.canModify(#id, authentication.principal.id)")
    public ResponseEntity<AssignmentResponse> updateAssignment(@PathVariable Long id, @RequestBody @Valid AssignmentUpdateRequest request) {
        return ResponseEntity.ok(assignmentService.updateAssignment(id, request));
    }
    @PatchMapping("/{assignmentId}/submissions/{submissionId}")
    @PreAuthorize("@assignmentService.canModifySubmission(#submissionId, authentication.principal.id) or hasRole('ADMIN')")
    public ResponseEntity<AssignmentSubmissionResponse> updateSubmission(
            @PathVariable Long assignmentId,
            @PathVariable Long submissionId,
            @RequestBody @Valid AssignmentSubmissionRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                assignmentService.updateSubmission(submissionId, request, user.getId())
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@assignmentService.canModify(#id, authentication.principal.id)")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}