package com.example.lmsproject.assignment.dto.response;


import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
@Data
public class AssignmentSubmissionResponse {
    private Long id;
    private String submissionText;
    private Integer grade;
    private String feedback;
    private LocalDateTime submittedAt;
    private Long assignmentId;
    private String assignmentTitle;
    private Long studentId;
    private String studentEmail;

}
