package com.example.lmsproject.content.controller;
import com.example.lmsproject.content.dto.request.ContentCreateRequest;
import com.example.lmsproject.content.dto.response.ContentResponse;
import com.example.lmsproject.content.dto.request.ContentUpdateRequest;
import com.example.lmsproject.content.service.ContentService;
import com.example.lmsproject.security.model.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/contents")
public class ContentController {
    private final ContentService contentService;
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }
    @PostMapping
    @PreAuthorize("@contentService.canCreateForCourse(#request.courseId(), authentication.principal.id) or hasRole('ADMIN')")
    public ResponseEntity<ContentResponse> createContent(
            @RequestBody ContentCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contentService.createContent(request, user.getId()));
    }
    @GetMapping("/{id}")
    @PreAuthorize("@contentService.canAccess(#id,authentication.principal.id)")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(contentService.getContentById(id, user.getId(), user.getAuthorities()));
    }
    @GetMapping("/course/{courseId}")
    @PreAuthorize("@courseService.isOwner(#courseId, authentication.principal.id) or hasRole('ADMIN')")
    public ResponseEntity<List<ContentResponse>> getContentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(contentService.getContentsByCourse(courseId));
    }
    @PatchMapping("/{id}")
    @PreAuthorize("@contentService.canModify(#id,authentication.principal.id)")
    public ResponseEntity<ContentResponse> updateContent(
            @PathVariable Long id,
            @RequestBody ContentUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(contentService.updateContent(id, request, user.getId()));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("@contentService.canModify(#id,authentication.principal.id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteContent(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user) {
        contentService.deleteContent(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
