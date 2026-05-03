package com.example.lmsproject.assessment.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentSubmissionResponse {

    private Long id;
    private Long assessmentId;
    private Long studentId;
    private Integer totalScore;
    private LocalDateTime submittedAt;
    private List<AnswerResponse> answers;
}