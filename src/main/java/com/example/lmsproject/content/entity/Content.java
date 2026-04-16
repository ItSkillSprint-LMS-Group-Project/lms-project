package com.example.lmsproject.content.entity;

import com.example.lmsproject.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contents")
@Getter @Setter
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private ContentType type;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}