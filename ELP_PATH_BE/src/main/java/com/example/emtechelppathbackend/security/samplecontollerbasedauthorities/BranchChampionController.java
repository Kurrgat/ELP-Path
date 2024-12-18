package com.example.emtechelppathbackend.security.samplecontollerbasedauthorities;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/branch-champion")
public class BranchChampionController {
    @GetMapping("/home")
    @PreAuthorize("hasAuthority('branchChampion:read')")
    public String home() {
        return "This is the branch champion proposed homepage, accessible branch champions only";
    }

    @GetMapping("/read")
    @PreAuthorize("hasAuthority('branchChampion:read')" + "|| hasAuthority('admin:read')")
    public String get() {
        return "GET::branchChampionChampion controller, accessible to admins and branch champions";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('branchChampion:create')" + "|| hasAuthority('admin:create')")
    public String post() {
        return "POST::branchChampionChampion controller";
    }

    @GetMapping("/update")
    @PreAuthorize("hasAuthority('branchChampion:update')" + "|| hasAuthority('admin:update')")
    public String put() {
        return "UPDATE::branchChampionChampion controller";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('branchChampion:delete')" + "|| hasAuthority('admin:delete')")
    public String delete() {
        return "DELETE::branchChampion controller";
    }
}

