package com.example.emtechelppathbackend.profile.profiletracker;

import lombok.Data;
import java.util.ArrayList;
@Data
public class AchievementsTracker {
    int percentageDone;
    ArrayList<String> pending = new ArrayList<>();

}
