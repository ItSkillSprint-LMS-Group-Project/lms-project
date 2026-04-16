package com.example.lmsproject.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter @Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean isPublished;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @OneToMany(mappedBy = "course")
    private List<Content> contents;

    @OneToMany(mappedBy = "course")
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "course")
    private List<Assessment> assessments;
}