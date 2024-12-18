package com.example.emtechelppathbackend.events;

import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.image.Image;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_details")
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "include the name of the event")
    private String eventName;

    @Column(updatable = false)
    private LocalDateTime recordDate = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    @Lob
    @NotNull(message = "Include a brief description of the event")
    private String eventDescription;


    private String eventDate;

    private String eventLink;

    private String location;

    @NotNull(message = "Include the name of the organizer")
    private String organizer;

    @OneToOne(cascade = CascadeType.ALL)
    private Image eventImage;

    @ManyToOne
    private Chapter chapter;

    @ManyToOne
    private Hub hub;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.SCHEDULED;
}


