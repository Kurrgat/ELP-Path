package com.example.emtechelppathbackend.comment;

import com.example.emtechelppathbackend.feed.feedv2.FeedsV2;
import com.example.emtechelppathbackend.security.user.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;




    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    @ManyToOne()
    @JoinColumn(name = "feed_id")
    @JsonIgnore
    private FeedsV2 feed;

    public void setFeed(FeedsV2 feed) {
        this.feed= feed;
    }

    public void setUsers(Users user) {

    }
}
