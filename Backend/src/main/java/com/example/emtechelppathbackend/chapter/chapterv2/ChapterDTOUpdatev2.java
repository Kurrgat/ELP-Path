package com.example.emtechelppathbackend.chapter.chapterv2;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChapterDTOUpdatev2 {
    private String chapterName;
    private String chapterDescription;
    private MultipartFile chapterImage;

}
