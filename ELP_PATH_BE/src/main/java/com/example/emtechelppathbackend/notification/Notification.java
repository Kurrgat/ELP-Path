package com.example.emtechelppathbackend.notification;

import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.security.user.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Column(nullable = false)
    private boolean viewed = false;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventsV2 events;

    @ManyToOne
    @JoinColumn(name = "hub_id")
    private Hubv2 hubs;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private ChapterV2 chapter;
}
