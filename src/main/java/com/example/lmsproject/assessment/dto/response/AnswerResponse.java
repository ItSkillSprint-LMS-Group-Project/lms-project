package com.example.lmsproject.assessment.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {

    private Long id;
    private Long questionId;
    private Long selectedOptionId;
    private String textAnswer;
}