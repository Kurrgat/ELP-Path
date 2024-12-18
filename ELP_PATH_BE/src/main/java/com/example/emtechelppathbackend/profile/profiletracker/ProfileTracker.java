package com.example.emtechelppathbackend.profile.profiletracker;


import java.util.ArrayList;

import lombok.Data;

@Data
public class ProfileTracker {
    int percentageDone;
    ArrayList<String> pending = new ArrayList<>();
}
