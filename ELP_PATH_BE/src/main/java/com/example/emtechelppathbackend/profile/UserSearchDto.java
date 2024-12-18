package com.example.emtechelppathbackend.profile;

import com.example.emtechelppathbackend.feed.feedv2.FeedsDTOv2;
import com.example.emtechelppathbackend.scholars.ScholarCategories;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserSearchDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private Long profileId;
    private String email;
    private String title;
    private String website;
    private String phoneNo;
    private String profileImage;
    private String currentCountryofResidence;
    private String currentCityofResidence;
    private LocalDateTime yearOfSelection;
    private JobStatus jobStatus;
//    private Branch homeBranch;
    private ScholarCategories scholarCategory;
    private List<FeedsDTOv2> feeds;
}
