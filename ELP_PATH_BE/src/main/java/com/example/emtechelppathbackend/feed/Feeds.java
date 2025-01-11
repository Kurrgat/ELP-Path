package com.example.emtechelppathbackend.feed;


import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.image.Image;
import com.example.emtechelppathbackend.security.user.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feeds")
public class Feeds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "description",length = 5000)
    private String description;


    @JsonFormat(pattern="yyyy-MM-dd:hh:mm:ss")
    @Column(name = "post_date")
    private LocalDateTime postDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "feed_images",
            joinColumns = { @JoinColumn(name = "feed_id") },
            inverseJoinColumns = { @JoinColumn(name = "image_id") })
    private Set<Image> images = new HashSet<>();

    @PrePersist
    private void onCreate(){
        postDate = LocalDateTime.now();
    }




}