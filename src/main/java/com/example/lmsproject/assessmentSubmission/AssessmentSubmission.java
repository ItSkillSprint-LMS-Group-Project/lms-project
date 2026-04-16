package com.example.lmsproject.assessmentSubmission;

import com.example.lmsproject.answer.Answer;
import com.example.lmsproject.assessment.Assessment;
import com.example.lmsproject.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assessment_submissions")
@Getter @Setter
public class AssessmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalScore;
    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @OneToMany(mappedBy = "submission")
    private List<Answer> answers;
}