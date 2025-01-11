package com.example.emtechelppathbackend.feed.feedv2;

import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class FeedsDTOv2 {
        private Long id;

        private String description;
        private String postDate;
        private Boolean toxic;
        private Boolean severeToxic;
        private Boolean obscene;
        private Boolean threat;
        private Boolean insult;
        private Boolean identityHate;
        private ChapterV2 chapter;
        private Hubv2 hub;
        private Long regionOrInstitutionId;
//        @JsonIgnore
        private UsersDto user;
        private Set<String> images = new HashSet<>();
}