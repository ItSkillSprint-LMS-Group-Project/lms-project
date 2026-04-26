package com.example.lmsproject.assessment.service;

import com.example.lmsproject.assessment.dto.request.SubmitAnswerRequest;
import com.example.lmsproject.assessment.dto.request.SubmitAssessmentRequest;
import com.example.lmsproject.assessment.dto.response.AssessmentSubmissionResponse;
import com.example.lmsproject.assessment.entity.*;
import com.example.lmsproject.assessment.mapper.SubmissionMapper;
import com.example.lmsproject.assessment.repository.AssessmentSubmissionRepository;
import com.example.lmsproject.exception.BadRequestException;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.entity.UserRole;
import com.example.lmsproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final AssessmentSubmissionRepository submissionRepository;
    private final AssessmentService assessmentService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final UserRepository userRepository;
    private final SubmissionMapper submissionMapper;

    public AssessmentSubmissionResponse submitAssessment(Long studentId, SubmitAssessmentRequest request) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (student.getRole() != UserRole.STUDENT) {
            throw new BadRequestException("Only students can submit assessments");
        }

        Assessment assessment = assessmentService.findAssessmentById(request.assessmentId());

        if (submissionRepository.existsByAssessmentIdAndStudentId(assessment.getId(), student.getId())) {
            throw new BadRequestException("You have already submitted this assessment");
        }

        AssessmentSubmission submission = new AssessmentSubmission();
        submission.setAssessment(assessment);
        submission.setStudent(student);

        List<Answer> answers = new ArrayList<>();
        int totalScore = 0;

        for (SubmitAnswerRequest answerRequest : request.answers()) {
            Question question = questionService.findQuestionById(answerRequest.questionId());

            if (!question.getAssessment().getId().equals(assessment.getId())) {
                throw new BadRequestException("Question does not belong to this assessment");
            }

            Answer answer = new Answer();
            answer.setSubmission(submission);
            answer.setQuestion(question);

            if (question.getType() == QuestionType.MCQ) {
                if (answerRequest.selectedOptionId() == null) {
                    throw new BadRequestException("Selected option is required for MCQ questions");
                }

                Option selectedOption = optionService.findOptionById(answerRequest.selectedOptionId());

                if (!selectedOption.getQuestion().getId().equals(question.getId())) {
                    throw new BadRequestException("Selected option does not belong to this question");
                }

                answer.setSelectedOption(selectedOption);

                if (selectedOption.isCorrect()) {
                    totalScore += question.getPoints();
                }
            }

            if (question.getType() == QuestionType.OPEN) {
                if (answerRequest.textAnswer() == null || answerRequest.textAnswer().isBlank()) {
                    throw new BadRequestException("Text answer is required for open questions");
                }

                answer.setTextAnswer(answerRequest.textAnswer());
            }

            answers.add(answer);
        }

        submission.setTotalScore(totalScore);
        submission.setAnswers(answers);

        AssessmentSubmission savedSubmission = submissionRepository.save(submission);

        return submissionMapper.toResponse(savedSubmission);
    }

    public List<AssessmentSubmissionResponse> getSubmissionsByAssessment(Long assessmentId) {
        Assessment assessment = assessmentService.findAssessmentById(assessmentId);

        return submissionRepository.findByAssessmentId(assessment.getId())
                .stream()
                .map(submissionMapper::toResponse)
                .toList();
    }

    public List<AssessmentSubmissionResponse> getSubmissionsByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return submissionRepository.findByStudentId(student.getId())
                .stream()
                .map(submissionMapper::toResponse)
                .toList();
    }

    public AssessmentSubmissionResponse getSubmissionById(Long id) {
        AssessmentSubmission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        return submissionMapper.toResponse(submission);
    }
}