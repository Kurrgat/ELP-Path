package com.example.emtechelppathbackend.hubs;

import com.example.emtechelppathbackend.feed.Feeds;
import com.example.emtechelppathbackend.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table(name = "Hubs")
public class Hub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hubName;

    private String hubDescription;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image hubImage;


}
