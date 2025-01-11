package com.example.emtechelppathbackend.events.eventsv2;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.List;


@Data
public class EventsDtov2 {
    private String eventName;
    private String eventDescription;

    private LocalDateTime eventDate;
    private String eventLink;
    private String location;
    private String organizer;
    List<MultipartFile> eventImage;
}