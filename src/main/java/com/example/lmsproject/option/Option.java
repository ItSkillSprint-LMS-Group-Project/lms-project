package com.example.lmsproject.option;

import com.example.lmsproject.question.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "options")
@Getter @Setter
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}