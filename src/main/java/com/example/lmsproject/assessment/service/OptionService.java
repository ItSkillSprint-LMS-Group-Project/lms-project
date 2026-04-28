package com.example.lmsproject.assessment.service;

import com.example.lmsproject.assessment.dto.request.CreateOptionRequest;
import com.example.lmsproject.assessment.dto.response.OptionTeacherResponse;
import com.example.lmsproject.assessment.entity.Option;
import com.example.lmsproject.assessment.entity.Question;
import com.example.lmsproject.assessment.mapper.QuestionMapper;
import com.example.lmsproject.assessment.repository.OptionRepository;
import com.example.lmsproject.exception.BadRequestException;
import com.example.lmsproject.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    public OptionTeacherResponse createOption(CreateOptionRequest request) {
        Question question = questionService.findQuestionById(request.questionId());

        if (question.getType().name().equals("OPEN")) {
            throw new BadRequestException("Open questions cannot have options");
        }

        Option option = new Option();
        option.setText(request.text());
        option.setCorrect(request.correct());
        option.setQuestion(question);

        return questionMapper.toTeacherOptionResponse(optionRepository.save(option));
    }

    public List<OptionTeacherResponse> getOptionsByQuestion(Long questionId) {
        return optionRepository.findByQuestionId(questionId)
                .stream()
                .map(questionMapper::toTeacherOptionResponse)
                .toList();
    }

    public Option findOptionById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found"));
    }

    public void deleteOption(Long id) {
        Option option = findOptionById(id);
        optionRepository.delete(option);
    }
}