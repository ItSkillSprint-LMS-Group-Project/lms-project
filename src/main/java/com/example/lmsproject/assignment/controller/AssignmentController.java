package com.example.lmsproject.assignment.controller;

import com.example.lmsproject.assignment.dto.request.AssignmentRequest;
import com.example.lmsproject.assignment.dto.response.AssignmentResponse;
import com.example.lmsproject.assignment.dto.request.AssignmentUpdateRequest;
import com.example.lmsproject.assignment.service.AssignmentService;
import com.example.lmsproject.security.model.CustomUserDetails;
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
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AssignmentResponse> createAssignment(
            @RequestBody AssignmentRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.createAssignment(request, user.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<AssignmentResponse> getAssignmentById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id, user.getId(), user.getAuthorities()));
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

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AssignmentResponse> updateAssignment(
            @PathVariable Long id,
            @RequestBody AssignmentUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(assignmentService.updateAssignment(id, request, user.getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user) {
        assignmentService.deleteAssignment(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
