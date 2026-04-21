package com.example.lmsproject.assessment.entity;

import com.example.lmsproject.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assessment_submissions")
@Getter @Setter
public class AssessmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer totalScore;

    @CreationTimestamp
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