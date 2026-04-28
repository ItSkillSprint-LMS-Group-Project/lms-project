package com.example.lmsproject.assessment.service;

import com.example.lmsproject.assessment.dto.request.CreateQuestionRequest;
import com.example.lmsproject.assessment.dto.response.QuestionResponse;
import com.example.lmsproject.assessment.entity.Assessment;
import com.example.lmsproject.assessment.entity.Question;
import com.example.lmsproject.assessment.mapper.QuestionMapper;
import com.example.lmsproject.assessment.repository.QuestionRepository;
import com.example.lmsproject.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AssessmentService assessmentService;
    private final QuestionMapper questionMapper;

    public QuestionResponse createQuestion(CreateQuestionRequest request) {
        Assessment assessment = assessmentService.findAssessmentById(request.assessmentId());

        Question question = new Question();
        question.setText(request.text());
        question.setType(request.type());
        question.setPoints(request.points());
        question.setAssessment(assessment);

        return questionMapper.toStudentResponse(questionRepository.save(question));
    }

    public List<QuestionResponse> getQuestionsByAssessment(Long assessmentId) {
        return questionRepository.findByAssessmentId(assessmentId)
                .stream()
                .map(questionMapper::toStudentResponse)
                .toList();
    }

    public Question findQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
    }

    public void deleteQuestion(Long id) {
        Question question = findQuestionById(id);
        questionRepository.delete(question);
    }
}