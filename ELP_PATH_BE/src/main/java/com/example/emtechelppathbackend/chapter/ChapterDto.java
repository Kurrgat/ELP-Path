package com.example.emtechelppathbackend.chapter;

import com.example.emtechelppathbackend.image.Image;
import lombok.Data;


@Data
public class ChapterDto {
    private Long id;
    private String chapterName;
    private String description;
    private ChapterType chapterType;
    private String chapterImage;
    private Long leaderId;
    private String leaderName;
}
