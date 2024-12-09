package com.example.emtechelppathbackend.activities.activitiesv2;

import com.example.emtechelppathbackend.activitytypes.activitytypesv2.ActivityTypeV2;
import com.example.emtechelppathbackend.chapter.ChapterV2;

import com.example.emtechelppathbackend.security.user.UsersDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
@Data
public class ActivityDTOViewv2 {
    private String activityName;
    private String activityDescription;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activityDate;
    private ActivityTypeV2 activityType;
    private String activityLocation;
    private Integer contribution;
    private String activityImage;
    private Set<UsersDto> attendees;
    private ChapterV2 chapter;
}
