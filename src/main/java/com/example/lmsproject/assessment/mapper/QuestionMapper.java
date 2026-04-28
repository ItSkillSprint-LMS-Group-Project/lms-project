package com.example.lmsproject.assessment.mapper;

import com.example.lmsproject.assessment.dto.response.OptionResponse;
import com.example.lmsproject.assessment.dto.response.OptionTeacherResponse;
import com.example.lmsproject.assessment.dto.response.QuestionResponse;
import com.example.lmsproject.assessment.entity.Option;
import com.example.lmsproject.assessment.entity.Question;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionMapper {

    public QuestionResponse toStudentResponse(Question question) {
        List<OptionResponse> options = question.getOptions() == null
                ? List.of()
                : question.getOptions()
                .stream()
                .map(this::toOptionResponse)
                .toList();

        return new QuestionResponse(
                question.getId(),
                question.getText(),
                question.getType(),
                question.getPoints(),
                question.getAssessment().getId(),
                options
        );
    }

    public OptionResponse toOptionResponse(Option option) {
        return new OptionResponse(
                option.getId(),
                option.getText()
        );
    }

    public OptionTeacherResponse toTeacherOptionResponse(Option option) {
        return new OptionTeacherResponse(
                option.getId(),
                option.getText(),
                option.isCorrect()
        );
    }
}