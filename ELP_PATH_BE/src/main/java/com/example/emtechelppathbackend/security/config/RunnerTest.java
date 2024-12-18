package com.example.emtechelppathbackend.security.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.emtechelppathbackend.profile.profiletracker.UpdateReminderService;

@Component
public class RunnerTest implements ApplicationRunner{
    private final UpdateReminderService updateReminderService;

    public RunnerTest(UpdateReminderService updateReminderService) {
        this.updateReminderService = updateReminderService;
    }

    @Override
    public void run(ApplicationArguments args) {
        // updateReminderService.sendUpdateReminderToAll();
    }
}
