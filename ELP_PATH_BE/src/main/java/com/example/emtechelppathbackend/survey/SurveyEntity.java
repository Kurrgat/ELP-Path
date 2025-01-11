package com.example.emtechelppathbackend.survey;



import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.chapter.ChapterV2;

import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.survey.questions.AnswersToQuestions;
import com.example.emtechelppathbackend.survey.questions.Questions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mockito.Answers;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="surveys")
public class SurveyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Questions>questions=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "chapter_id")

    private ChapterV2 chapter;

    @ManyToOne
    @JoinColumn(name = "hub_id")

    private Hubv2 hub;

}
