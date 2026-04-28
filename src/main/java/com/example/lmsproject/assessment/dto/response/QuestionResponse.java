package com.example.lmsproject.assessment.dto.response;

import com.example.lmsproject.assessment.entity.QuestionType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {

    private Long id;
    private String text;
    private QuestionType type;
    private Integer points;
    private Long assessmentId;
    private List<OptionResponse> options;
}