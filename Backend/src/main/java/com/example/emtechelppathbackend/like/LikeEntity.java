package com.example.emtechelppathbackend.like;

import com.example.emtechelppathbackend.feed.feedv2.FeedsV2;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.sportlight.SportLight;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")

    private Users user;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    @JsonIgnore
    private FeedsV2 feed;


}
