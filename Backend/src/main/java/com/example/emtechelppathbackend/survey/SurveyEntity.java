package com.example.emtechelppathbackend.survey;



import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.chapter.ChapterV2;

import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String question;



    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private ChapterV2 chapter;

    @ManyToOne
    @JoinColumn(name = "hub_id")
    private Hubv2 hub;

    private Long regionOrInstitutionId;



}
