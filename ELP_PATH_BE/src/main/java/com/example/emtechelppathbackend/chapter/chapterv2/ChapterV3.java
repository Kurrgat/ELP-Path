//package com.example.emtechelppathbackend.chapter.chapterv2;
//
//import com.example.emtechelppathbackend.scholars.Scholar;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//
//@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@ToString
//public class ChapterV3 {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String chapterName;
//
//    private String nickName;
//
//    private ChapterTypev2 chapterType;
//
//    private String chapterDescription;
//
//    private String chapterImage;
//
//    private Long regionOrInstitutionId;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    private Scholar chapterLeader;
//}