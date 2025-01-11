package com.example.emtechelppathbackend.events.eventsv2;


import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.reward.userpoints.UserPoints;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.ApprovalType;
import com.example.emtechelppathbackend.utils.EventApprovalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "eventv2_details")
public class EventsV2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "include the name of the event")
    private String eventName;

    @Column(updatable = false)
    private LocalDateTime recordDate = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    @Length(max = 10000)
    @Lob
    @NotNull(message = "Include a brief description of the event")
    private String eventDescription;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime eventDate;

    private String eventLink;

    private String location;

    @Enumerated(EnumType.STRING)
    private ApprovalType approvalType;

    @NotNull(message = "Include the name of the organizer")
    private String organizer;


    @ElementCollection
    @CollectionTable(name = "eventv2_images")
    private Set<String> eventImages = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    @JsonIgnore
    private ChapterV2 chapter;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne
    @JsonIgnore
    private Hubv2 hub;

    @Enumerated(EnumType.STRING)
    private EventStatusv2 eventStatus = EventStatusv2.SCHEDULED;

    @Enumerated(EnumType.STRING)
    private EventApprovalStatus approvalStatus = EventApprovalStatus.PENDING;


}


