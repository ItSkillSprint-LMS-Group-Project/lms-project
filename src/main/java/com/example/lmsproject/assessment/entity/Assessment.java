package com.example.lmsproject.assessment.entity;

import com.example.lmsproject.course.entity.Course;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer timeLimitMinutes;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "assessment")
    private List<Question> questions;
}