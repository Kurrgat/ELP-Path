package com.example.emtechelppathbackend.profile;

import lombok.Data;

@Data
public class MorePeopleDto {
    private Long userId;
    private Long profileId;
    private String scholarName;
    private String branchName;
    private String profileImage;
    private String school;
    private String institution;
}
