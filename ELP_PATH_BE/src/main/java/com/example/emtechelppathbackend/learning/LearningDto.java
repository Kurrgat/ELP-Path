package com.example.emtechelppathbackend.learning;

import com.example.emtechelppathbackend.utils.LearningCategory;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class LearningDto {
    private String title;
    private LearningCategory category;
    private String courseName;
    private String description;
    private List<String> objectives;
    private MultipartFile image;
    private String link;
    private MultipartFile document;

}
