package com.example.emtechelppathbackend.survey;

import com.example.emtechelppathbackend.survey.questions.AnswersToQuestions;
import com.example.emtechelppathbackend.survey.questions.Questions;
import lombok.Data;

import java.util.List;

@Data
public class SurveyDto {
    private String title;
    private List<Questions> questionsList;
    private List<AnswersToQuestions>answers;
}
