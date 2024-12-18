package com.example.emtechelppathbackend.profile.profiletracker;


import lombok.Data;

@Data
public class ProgressResponse {
    int percentage;
    String message;
    ProfileTracker profileTracker;
    BioTracker bioTracker;
    EducationTracker educationTracker;
    SkillsTracker skillsTracker;
    CareerTracker careerTracker;
    AchievementsTracker achievementsTracker;

}
