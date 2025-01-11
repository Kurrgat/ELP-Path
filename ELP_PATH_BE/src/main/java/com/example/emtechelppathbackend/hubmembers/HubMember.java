package com.example.emtechelppathbackend.hubmembers;

import com.example.emtechelppathbackend.hubs.Hub;
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
@Table(name = "hub_member")
public class HubMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hub_id")
    private Hub hub;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users member;

    @Column(name = "date_joined")
    private LocalDateTime joiningDate;

    @Column(name = "date_left")
    private LocalDateTime leavingDate;

    private boolean activeMembership;
}
