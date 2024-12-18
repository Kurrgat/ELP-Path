package com.example.emtechelppathbackend.chapter;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChapterDTOUpdate {
    private String chapterName;
    private String chapterDescription;
    private MultipartFile chapterImage;

}
