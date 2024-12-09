package com.example.emtechelppathbackend.activities;

import com.example.emtechelppathbackend.activitytypes.ActivityType;
import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ActivityDTO {
	private String activityName;
	private String activityDescription;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime activityDate;
	private ActivityType activityType;
	private String activityLocation;
	private Integer contribution;
	private MultipartFile activityImage;
	private Set<UsersDto> attendees;
	private Chapter chapter;
}