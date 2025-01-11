package com.example.emtechelppathbackend.feed;

import com.example.emtechelppathbackend.image.Image;
import com.example.emtechelppathbackend.security.user.UsersDto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class FeedsDto {
        private Long id;
        private String description;
        private LocalDateTime postDate;
        private UsersDto user;
        private Set<Image> image = new HashSet<>();
}
