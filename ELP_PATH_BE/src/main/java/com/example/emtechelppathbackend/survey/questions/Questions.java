package com.example.emtechelppathbackend.survey.questions;

import com.example.emtechelppathbackend.survey.SurveyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "questions")
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    @ManyToOne
    @JoinColumn(name = "survey_id")
    private SurveyEntity survey;
    @OneToMany
    private List<AnswersToQuestions>answers=new ArrayList<>();

}
