package com.example.emtechelppathbackend.events;

import com.example.emtechelppathbackend.chapter.ChapterDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class EventsDto {
    private String eventName;
    private String eventDescription;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private String eventLink;
    private String location;
    private String organizer;
    private MultipartFile eventImage;
    private ChapterDto chapter;
}