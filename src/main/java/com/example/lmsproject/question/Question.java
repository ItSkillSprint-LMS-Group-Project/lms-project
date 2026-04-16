package com.example.lmsproject.question;

import com.example.lmsproject.assessment.Assessment;
import com.example.lmsproject.option.Option;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter @Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;
    private Integer points;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @OneToMany(mappedBy = "question")
    private List<Option> options;
}