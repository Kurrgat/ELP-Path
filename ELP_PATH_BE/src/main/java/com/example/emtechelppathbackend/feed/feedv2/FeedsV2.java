package com.example.emtechelppathbackend.feed.feedv2;

import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.chapter.ChapterType;
import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.security.user.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class FeedsV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "description",length = 5000)
    private String description;


    @JsonFormat(pattern="yyyy-MM-dd:hh:mm:ss")
    @Column(name = "post_date")
    private LocalDateTime postDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ElementCollection
    @CollectionTable(name = "feedv2_images")
    private Set<String> images = new HashSet<>();

    @PrePersist
    private void onCreate(){
        postDate = LocalDateTime.now();
    }

    @Column(columnDefinition = "TINYINT DEFAULT false")
    private Boolean toxic;
    @Column(columnDefinition = "TINYINT DEFAULT false")
    private Boolean severe_toxic;
    @Column(columnDefinition = "TINYINT DEFAULT false")
    private Boolean obscene;
    @Column(columnDefinition = "TINYINT DEFAULT false")
    private Boolean threat;
    @Column(columnDefinition = "TINYINT DEFAULT false")
    private Boolean insult;
    @Column(columnDefinition = "TINYINT DEFAULT false")
    private Boolean identity_hate;

    private Long regionOrInstitutionId;
    @ManyToOne
    @JoinColumn(name = "chapter_id")

    private ChapterV2 chapter; // New association with Chapter

    @ManyToOne
    @JoinColumn(name = "hub_id")

    private Hubv2 hub; // New association with Hub








}