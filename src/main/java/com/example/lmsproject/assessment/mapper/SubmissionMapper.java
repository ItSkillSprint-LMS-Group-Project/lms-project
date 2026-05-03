package com.example.lmsproject.assessment.mapper;

import com.example.lmsproject.assessment.dto.response.AnswerResponse;
import com.example.lmsproject.assessment.dto.response.AssessmentSubmissionResponse;
import com.example.lmsproject.assessment.entity.Answer;
import com.example.lmsproject.assessment.entity.AssessmentSubmission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubmissionMapper {

    public AssessmentSubmissionResponse toResponse(AssessmentSubmission submission) {
        List<AnswerResponse> answers = submission.getAnswers()
                .stream()
                .map(this::toAnswerResponse)
                .toList();

        return new AssessmentSubmissionResponse(
                submission.getId(),
                submission.getAssessment().getId(),
                submission.getStudent().getId(),
                submission.getTotalScore(),
                submission.getSubmittedAt(),
                answers
        );
    }

    private AnswerResponse toAnswerResponse(Answer answer) {
        Long selectedOptionId = answer.getSelectedOption() != null
                ? answer.getSelectedOption().getId()
                : null;

        return new AnswerResponse(
                answer.getId(),
                answer.getQuestion().getId(),
                selectedOptionId,
                answer.getTextAnswer()
        );
    }
}