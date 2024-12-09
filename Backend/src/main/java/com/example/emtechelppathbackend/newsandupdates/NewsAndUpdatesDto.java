package com.example.emtechelppathbackend.newsandupdates;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class NewsAndUpdatesDto {
    private String title;
    private String message;
    private MultipartFile image;
}
