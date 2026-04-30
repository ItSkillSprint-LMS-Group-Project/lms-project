package com.example.lmsproject.assignment.entity;

import com.example.lmsproject.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assignments")
@Getter @Setter
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;

    @Column
    private String description;

    @Column
    private LocalDateTime dueDate;

    @Column
    private String feedback;

    @Column
    private Integer grade=0;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne(mappedBy = "assignment")
    private AssignmentSubmission submission;
}