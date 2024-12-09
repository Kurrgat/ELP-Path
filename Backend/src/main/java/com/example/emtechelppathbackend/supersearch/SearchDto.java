package com.example.emtechelppathbackend.supersearch;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
public class SearchDto {
    @Id
    private Long id;
    private String content;
    private String type;

}

