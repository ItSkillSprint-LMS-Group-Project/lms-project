package com.example.lmsproject.assessment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "options")
@Getter @Setter
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;
    @Column
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}