package com.example.lmsproject.enrollment.controller;

import com.example.lmsproject.enrollment.dto.EnrollByCodeRequest;
import com.example.lmsproject.enrollment.dto.EnrollByEmailRequest;
import com.example.lmsproject.enrollment.dto.EnrollmentResponse;
import com.example.lmsproject.enrollment.service.EnrollmentService;
import com.example.lmsproject.security.model.CustomUserDetails;
import com.example.lmsproject.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    public EnrollmentController(EnrollmentService enrollmentService){
        this.enrollmentService=enrollmentService;
    }
    @PostMapping("/code")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> enrollByCourseCode(
            @RequestBody EnrollByCodeRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enrollByCourseCode(request, user.getId()));
    }
    @PostMapping("/{courseId}/email")
    @PreAuthorize("@courseService.isOwner(#courseId,authentication.principal.id)")
    public ResponseEntity<EnrollmentResponse>  enrollByEmail(@PathVariable Long courseId,@RequestBody EnrollByEmailRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enrollByEmail(courseId,request));
    }
    @GetMapping("/my-enrollments")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentResponse>> enrollmentsByStudent(@AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(enrollmentService.enrollmentsByStudent(user.getId()));
    }
    @GetMapping("/my-students")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<EnrollmentResponse>> getMyStudents(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                enrollmentService.getStudentsByTeacher(user.getId())
        );
    }
}
