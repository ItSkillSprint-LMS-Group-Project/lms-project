package com.example.lmsproject.assessment.controller;

import com.example.lmsproject.assessment.dto.request.CreateOptionRequest;
import com.example.lmsproject.assessment.dto.response.OptionTeacherResponse;
import com.example.lmsproject.assessment.service.OptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class OptionController {

    private final OptionService optionService;

    @PreAuthorize("hasRole('ADMIN') or @assessmentService.canModifyQuestion(#request.questionId(), authentication.principal.id)")
    @PostMapping
    public ResponseEntity<OptionTeacherResponse> createOption(@Valid @RequestBody CreateOptionRequest request) {
        return ResponseEntity.status(201).body(optionService.createOption(request));
    }

    @PreAuthorize("hasRole('ADMIN') or @assessmentService.canModifyQuestion(#questionId, authentication.principal.id)")
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<OptionTeacherResponse>> getOptionsByQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(optionService.getOptionsByQuestion(questionId));
    }

    @PreAuthorize("hasRole('ADMIN') or @assessmentService.canModifyOption(#id, authentication.principal.id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
        return ResponseEntity.ok("Option deleted successfully");
    }
}