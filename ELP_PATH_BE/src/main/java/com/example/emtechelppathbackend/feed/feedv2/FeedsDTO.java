package com.example.emtechelppathbackend.feed.feedv2;

import java.util.List;

import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.chapter.ChapterType;
import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FeedsDTO {
    private String title;
    private String description;
    List<MultipartFile> images;




}
