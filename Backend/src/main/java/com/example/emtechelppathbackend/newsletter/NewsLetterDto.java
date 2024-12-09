package com.example.emtechelppathbackend.newsletter;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class NewsLetterDto {

    private String title;
    private String description;
    private MultipartFile document;

}
