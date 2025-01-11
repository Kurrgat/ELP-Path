package com.example.emtechelppathbackend.profile;

import lombok.Data;

public interface MorePeopleInterface {
    Long getUserId();
    Long getProfileId();
    String getScholarName();
    String getBranchName();
    String getProfileImage();
    String getSchool();
    String getInstitution();

    void setProfileImage(String profileImage);
}
