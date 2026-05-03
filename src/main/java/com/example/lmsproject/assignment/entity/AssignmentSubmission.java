package com.example.lmsproject.assignment.entity;

import com.example.lmsproject.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_submissions")
@Getter @Setter
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String submissionText;
    @Column
    private Integer grade;

    @Column
    private String feedback;

    @CreationTimestamp
    private LocalDateTime submittedAt;

    @OneToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
}