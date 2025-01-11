package com.example.emtechelppathbackend.socialmedia;

import com.example.emtechelppathbackend.security.user.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "socials")
public class SocialMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String linkedIn;
    private String twitter;
    private String facebook;
    private String github;
    private String instagram;
    @JoinColumn(name = "users_id")
    @OneToOne
    private Users user;
}
