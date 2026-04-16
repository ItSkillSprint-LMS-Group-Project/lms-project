package com.example.lmsproject.assessment;

import com.example.lmsproject.course.Course;
import com.example.lmsproject.question.Question;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "assessments")
@Getter @Setter
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer orderIndex;
    private Integer timeLimitMinutes;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "assessment")
    private List<Question> questions;
}