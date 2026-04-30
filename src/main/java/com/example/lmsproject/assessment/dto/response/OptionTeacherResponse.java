package com.example.lmsproject.assessment.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionTeacherResponse {

    private Long id;
    private String text;
    private boolean correct;
}