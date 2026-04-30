package com.lmsproject;

import com.example.lmsproject.assessment.dto.request.SubmitAnswerRequest;
import com.example.lmsproject.assessment.dto.request.SubmitAssessmentRequest;
import com.example.lmsproject.assessment.dto.response.AssessmentSubmissionResponse;
import com.example.lmsproject.assessment.entity.*;
import com.example.lmsproject.assessment.mapper.SubmissionMapper;
import com.example.lmsproject.assessment.repository.AssessmentSubmissionRepository;
import com.example.lmsproject.assessment.service.AssessmentService;
import com.example.lmsproject.assessment.service.OptionService;
import com.example.lmsproject.assessment.service.QuestionService;
import com.example.lmsproject.assessment.service.SubmissionService;
import com.example.lmsproject.exception.BadRequestException;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.user.entity.User;
import com.example.lmsproject.user.entity.UserRole;
import com.example.lmsproject.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private AssessmentSubmissionRepository submissionRepository;

    @Mock
    private AssessmentService assessmentService;

    @Mock
    private QuestionService questionService;

    @Mock
    private OptionService optionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubmissionMapper submissionMapper;

    @InjectMocks
    private SubmissionService submissionService;

    @Test
    void submitAssessment_ShouldThrow_WhenUserNotStudent() {

        User user = new User();
        user.setRole(UserRole.TEACHER);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        SubmitAssessmentRequest request = new SubmitAssessmentRequest();
        request.setAssessmentId(1L);

        assertThrows(BadRequestException.class,
                () -> submissionService.submitAssessment(1L, request));
    }

    @Test
    void submitAssessment_ShouldThrow_WhenAlreadySubmitted() {

        User user = new User();
        user.setRole(UserRole.STUDENT);

        Assessment assessment = new Assessment();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(assessmentService.findAssessmentById(1L))
                .thenReturn(assessment);

        when(submissionRepository.existsByAssessmentIdAndStudentId(1L, user.getId()))
                .thenReturn(true);

        SubmitAssessmentRequest request = new SubmitAssessmentRequest();
        request.setAssessmentId(1L);

        assertThrows(BadRequestException.class,
                () -> submissionService.submitAssessment(1L, request));
    }

    @Test
    void submitAssessment_ShouldSubmitSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setRole(UserRole.STUDENT);

        Assessment assessment = new Assessment();
        assessment.setId(1L);

        Question question = new Question();
        question.setId(10L);
        question.setType(QuestionType.OPEN);
        question.setPoints(5);

        SubmitAnswerRequest answerRequest = new SubmitAnswerRequest();
        answerRequest.setQuestionId(10L);
        answerRequest.setTextAnswer("answer");

        SubmitAssessmentRequest request = new SubmitAssessmentRequest();
        request.setAssessmentId(1L);
        request.setAnswers(List.of(answerRequest));

        AssessmentSubmission submission = new AssessmentSubmission();
        AssessmentSubmissionResponse response = new AssessmentSubmissionResponse();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(assessmentService.findAssessmentById(1L))
                .thenReturn(assessment);

        when(submissionRepository.existsByAssessmentIdAndStudentId(1L, 1L))
                .thenReturn(false);

        when(questionService.findQuestionById(10L))
                .thenReturn(question);

        when(submissionRepository.save(any()))
                .thenReturn(submission);

        when(submissionMapper.toResponse(submission))
                .thenReturn(response);

        AssessmentSubmissionResponse result =
                submissionService.submitAssessment(1L, request);

        assertNotNull(result);
    }

    @Test
    void getSubmissionById_ShouldReturnSubmission() {

        AssessmentSubmission submission = new AssessmentSubmission();
        AssessmentSubmissionResponse response = new AssessmentSubmissionResponse();

        when(submissionRepository.findById(1L))
                .thenReturn(Optional.of(submission));

        when(submissionMapper.toResponse(submission))
                .thenReturn(response);

        AssessmentSubmissionResponse result =
                submissionService.getSubmissionById(1L);

        assertNotNull(result);
    }

    @Test
    void getSubmissionById_ShouldThrow_WhenNotFound() {

        when(submissionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> submissionService.getSubmissionById(1L));
    }
}