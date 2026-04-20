package com.example.lmsproject.course.controller;

import com.example.lmsproject.course.dto.CourseRequest;
import com.example.lmsproject.course.dto.CourseResponse;
import com.example.lmsproject.course.dto.CourseUpdateRequest;
import com.example.lmsproject.course.service.CourseService;
import com.example.lmsproject.security.model.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest, @AuthenticationPrincipal CustomUserDetails user){
        CourseResponse courseResponse = courseService.createCourse(courseRequest, user.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseResponse);
    }
    @GetMapping("/{id}")
    @PreAuthorize("@courseService.isOwner(#id, authentication.principal.id) or hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }
    @GetMapping("/my")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<CourseResponse>> getCourses(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(courseService.getCoursesByTeacher(user.getId()));
    }
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@courseService.isOwner(#id, authentication.principal.id) or hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseUpdateRequest request
    ) {
        return ResponseEntity.ok(courseService.updateCourse(id,request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@courseService.isOwner(#id, authentication.principal.id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
