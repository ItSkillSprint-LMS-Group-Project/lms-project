package com.lmsproject;

import com.example.lmsproject.assessment.dto.request.CreateQuestionRequest;
import com.example.lmsproject.assessment.dto.response.QuestionResponse;
import com.example.lmsproject.assessment.entity.Assessment;
import com.example.lmsproject.assessment.entity.Question;
import com.example.lmsproject.assessment.mapper.QuestionMapper;
import com.example.lmsproject.assessment.repository.QuestionRepository;
import com.example.lmsproject.assessment.service.AssessmentService;
import com.example.lmsproject.assessment.service.QuestionService;
import com.example.lmsproject.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AssessmentService assessmentService;

    @Mock
    private QuestionMapper questionMapper;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void createQuestion_success() {

        CreateQuestionRequest request = new CreateQuestionRequest();
        request.setAssessmentId(1L);

        Assessment assessment = new Assessment();

        Question question = new Question();

        QuestionResponse response = new QuestionResponse();

        when(assessmentService.findAssessmentById(1L))
                .thenReturn(assessment);

        when(questionMapper.toEntity(request, assessment))
                .thenReturn(question);

        when(questionRepository.save(question))
                .thenReturn(question);

        when(questionMapper.toStudentResponse(question))
                .thenReturn(response);

        QuestionResponse result = questionService.createQuestion(request);

        assertNotNull(result);

        verify(questionRepository).save(question);
    }

    @Test
    void getQuestionsByAssessment_success() {

        Question question = new Question();
        QuestionResponse response = new QuestionResponse();

        when(questionRepository.findByAssessmentId(1L))
                .thenReturn(List.of(question));

        when(questionMapper.toStudentResponse(question))
                .thenReturn(response);

        List<QuestionResponse> result =
                questionService.getQuestionsByAssessment(1L);

        assertEquals(1, result.size());
    }

    @Test
    void findQuestionById_success() {

        Question question = new Question();

        when(questionRepository.findById(1L))
                .thenReturn(Optional.of(question));

        Question result = questionService.findQuestionById(1L);

        assertNotNull(result);
    }

    @Test
    void findQuestionById_notFound() {

        when(questionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> questionService.findQuestionById(1L));
    }

    @Test
    void deleteQuestion_success() {

        Question question = new Question();

        when(questionRepository.findById(1L))
                .thenReturn(Optional.of(question));

        questionService.deleteQuestion(1L);

        verify(questionRepository).delete(question);
    }
}