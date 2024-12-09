package com.example.emtechelppathbackend.hubs;

import com.example.emtechelppathbackend.image.Image;
import lombok.Data;

@Data
public class HubDTO {
    private Long id;
    private String hubName;
    private String hubDescription;
    private Image hubImage;
}
