package com.example.lmsproject.answer;

import com.example.lmsproject.assessmentSubmission.AssessmentSubmission;
import com.example.lmsproject.option.Option;
import com.example.lmsproject.question.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answers")
@Getter @Setter
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private AssessmentSubmission submission;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private Option selectedOption;
}