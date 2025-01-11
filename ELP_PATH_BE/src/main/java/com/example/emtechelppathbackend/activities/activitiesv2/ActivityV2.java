package com.example.emtechelppathbackend.activities.activitiesv2;

import com.example.emtechelppathbackend.activitytypes.activitytypesv2.ActivityTypeV2;
import com.example.emtechelppathbackend.chapter.ChapterV2;

import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activity_detailsv2")
//@Table(name = "activity_details")
public class ActivityV2 {
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

    private ActivityTypeV2 activityType;

    private String activityLocation;

    @NotNull(message = "Specify amount to be contributed")
    private Integer contribution;

    private String activityImage;

    @ManyToOne
    @JsonIgnore
    @JoinColumn (name = "chapter1_id")
    private ChapterV2 chapter;

    @ManyToOne
    private Hubv2 hub;

    @Enumerated(EnumType.STRING)
    private ActivityStatusv2 activityStatus = ActivityStatusv2.SCHEDULED;

}

