package com.example.emtechelppathbackend.activities;

import com.example.emtechelppathbackend.activitytypes.ActivityType;
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
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activity_details")
//@Table(name = "activity_details")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime recordDate = LocalDateTime.now();

    @NotNull(message = "An activity must have a name")
    private String activityName;

    @Column(columnDefinition = "TEXT")
    @Lob
    @NotNull(message = "Give a brief description of your activity")
    private String activityDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activityDate;

    @ManyToOne
    @JoinColumn(name = "activityType")
    private ActivityType activityType;

    private String activityLocation;

    @NotNull(message = "Specify amount to be contributed")
    private Integer contribution;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image activityImage;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @ManyToOne
    @JoinColumn(name = "hub_id")
    private Hub hub;

    @Enumerated(EnumType.STRING)
    private ActivityStatus activityStatus = ActivityStatus.SCHEDULED;

}

