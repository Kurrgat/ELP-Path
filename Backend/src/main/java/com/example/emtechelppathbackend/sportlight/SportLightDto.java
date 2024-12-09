package com.example.emtechelppathbackend.sportlight;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SportLightDto {

    private String title;
    private String content;
    private MultipartFile image;


}
