package com.example.lmsproject.content;

import com.example.lmsproject.course.Course;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contents")
@Getter @Setter
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type;
    private String textContent;
    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}