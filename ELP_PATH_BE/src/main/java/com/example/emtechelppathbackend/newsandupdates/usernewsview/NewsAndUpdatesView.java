package com.example.emtechelppathbackend.newsandupdates.usernewsview;

import com.example.emtechelppathbackend.newsandupdates.NewsAndUpdates;
import com.example.emtechelppathbackend.security.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "news_view")
public class NewsAndUpdatesView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "news_and_updates_id")
    private NewsAndUpdates newsAndUpdates;
}
