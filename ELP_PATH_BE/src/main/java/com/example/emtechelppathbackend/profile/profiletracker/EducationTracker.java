package com.example.emtechelppathbackend.profile.profiletracker;

import java.util.ArrayList;

import lombok.Data;

@Data
public class EducationTracker {
    int percentageDone;
    ArrayList<String> pending = new ArrayList<>();

}
