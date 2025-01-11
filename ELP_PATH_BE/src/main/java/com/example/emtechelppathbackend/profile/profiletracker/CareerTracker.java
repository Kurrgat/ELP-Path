package com.example.emtechelppathbackend.profile.profiletracker;

import lombok.Data;
import java.util.ArrayList;

@Data
public class CareerTracker {
    int percentageDone;
    ArrayList<String> pending = new ArrayList<>();

}
