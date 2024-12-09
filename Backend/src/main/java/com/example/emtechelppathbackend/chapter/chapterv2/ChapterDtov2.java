package com.example.emtechelppathbackend.chapter.chapterv2;

import com.example.emtechelppathbackend.chapter.ChapterType;
import com.example.emtechelppathbackend.scholars.Scholar;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ChapterDtov2 {

    private String chapterName;
    private String chapterDescription;
    private ChapterType chapterType;
    private MultipartFile chapterImage;
    private String nickName;
    private Long regionOrInstitutionId;
    private Scholar chapterLeader;




}
