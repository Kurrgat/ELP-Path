package com.example.emtechelppathbackend.chapter;

import com.example.emtechelppathbackend.counties.KenyanCounty;
import com.example.emtechelppathbackend.scholars.Scholar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ChapterV2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chapterName;
    private String nickName;
    private String chapterImage;
    private String chapterDescription;

    @Enumerated(EnumType.STRING)
    private ChapterType chapterType;


    private Long regionOrInstitutionId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Scholar chapterLeader;




}
