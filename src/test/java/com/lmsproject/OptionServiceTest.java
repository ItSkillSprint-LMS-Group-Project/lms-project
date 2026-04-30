package com.lmsproject;

import com.example.lmsproject.assessment.dto.request.CreateOptionRequest;
import com.example.lmsproject.assessment.dto.response.OptionTeacherResponse;
import com.example.lmsproject.assessment.entity.Option;
import com.example.lmsproject.assessment.entity.Question;
import com.example.lmsproject.assessment.mapper.QuestionMapper;
import com.example.lmsproject.assessment.repository.OptionRepository;
import com.example.lmsproject.assessment.service.OptionService;
import com.example.lmsproject.assessment.service.QuestionService;
import com.example.lmsproject.exception.BadRequestException;
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
class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private QuestionService questionService;

    @Mock
    private QuestionMapper questionMapper;

    @InjectMocks
    private OptionService optionService;

    @Test
    void createOption_ShouldCreateSuccessfully() {

        CreateOptionRequest request = new CreateOptionRequest();
        request.setQuestionId(1L);
        request.setText("Option A");
        request.setCorrect(true);

        Question question = new Question();

        Option option = new Option();
        OptionTeacherResponse response = new OptionTeacherResponse();

        when(questionService.findQuestionById(1L))
                .thenReturn(question);

        when(optionRepository.save(any(Option.class)))
                .thenReturn(option);

        when(questionMapper.toTeacherOptionResponse(option))
                .thenReturn(response);

        OptionTeacherResponse result = optionService.createOption(request);

        assertNotNull(result);
        verify(optionRepository).save(any(Option.class));
    }

    @Test
    void createOption_ShouldThrow_WhenQuestionIsOpen() {

        CreateOptionRequest request = new CreateOptionRequest();
        request.setQuestionId(1L);

        Question question = new Question();
        question.setType(com.example.lmsproject.assessment.entity.QuestionType.OPEN);

        when(questionService.findQuestionById(1L))
                .thenReturn(question);

        assertThrows(BadRequestException.class,
                () -> optionService.createOption(request));
    }

    @Test
    void getOptionsByQuestion_ShouldReturnList() {

        Option option = new Option();
        OptionTeacherResponse response = new OptionTeacherResponse();

        when(optionRepository.findByQuestionId(1L))
                .thenReturn(List.of(option));

        when(questionMapper.toTeacherOptionResponse(option))
                .thenReturn(response);

        List<OptionTeacherResponse> result =
                optionService.getOptionsByQuestion(1L);

        assertEquals(1, result.size());
    }

    @Test
    void findOptionById_ShouldReturnOption() {

        Option option = new Option();

        when(optionRepository.findById(1L))
                .thenReturn(Optional.of(option));

        Option result = optionService.findOptionById(1L);

        assertNotNull(result);
    }

    @Test
    void findOptionById_ShouldThrow_WhenNotFound() {

        when(optionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> optionService.findOptionById(1L));
    }

    @Test
    void deleteOption_ShouldDeleteSuccessfully() {

        Option option = new Option();

        when(optionRepository.findById(1L))
                .thenReturn(Optional.of(option));

        optionService.deleteOption(1L);

        verify(optionRepository).delete(option);
    }
}