package com.example.emtechelppathbackend.hubmembers.hubmembersv2;

import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.security.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "hub_memberv2")
public class HubMemberv2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hub_id")
    private Hubv2 hub;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users member;

    @Column(name = "date_joined")
    private LocalDateTime joiningDate;

    @Column(name = "date_left")
    private LocalDateTime leavingDate;

    private boolean activeMembership;
}
