package com.example.emtechelppathbackend.survey.questions;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.survey.SurveyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "answers")
public class AnswersToQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String answer;


    @ManyToOne
    @JoinColumn(name ="question_id" )
    private Questions questions;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private SurveyEntity survey;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
}
