package com.example.lmsproject.course.entity;
import com.example.lmsproject.assessment.entity.Assessment;
import com.example.lmsproject.assignment.entity.Assignment;
import com.example.lmsproject.content.entity.Content;
import com.example.lmsproject.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter @Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(unique = true,nullable = false)
    private String courseCode;

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