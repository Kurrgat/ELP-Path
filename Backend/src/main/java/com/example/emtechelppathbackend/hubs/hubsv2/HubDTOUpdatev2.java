package com.example.emtechelppathbackend.hubs.hubsv2;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class HubDTOUpdatev2 {
    private String hubName;
    private String hubDescription;
    private MultipartFile hubImage;
}
