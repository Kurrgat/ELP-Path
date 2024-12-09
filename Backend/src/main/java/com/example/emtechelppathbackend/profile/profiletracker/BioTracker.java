package com.example.emtechelppathbackend.profile.profiletracker;

import java.util.ArrayList;

import lombok.Data;

@Data
public class BioTracker {
    int percentageDone;
    ArrayList<String> pending = new ArrayList<>();

}
