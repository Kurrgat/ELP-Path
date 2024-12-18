package com.example.emtechelppathbackend.reward.userpoints;



import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import com.example.emtechelppathbackend.security.user.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="user_points")
public class UserPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int points;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)

    private Users user;




}
