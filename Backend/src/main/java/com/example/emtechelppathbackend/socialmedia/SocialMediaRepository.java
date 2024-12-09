package com.example.emtechelppathbackend.socialmedia;


import com.example.emtechelppathbackend.socialmedia.SocialMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocialMediaRepository extends JpaRepository<SocialMedia,Long> {
    SocialMedia findByUserId(Long userId);

}
