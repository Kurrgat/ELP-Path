package com.example.emtechelppathbackend.hubs.hubsv2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table(name = "Hubsv2")
public class Hubv2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hubName;

    private String hubDescription;

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "image_id")
    // private Image hubImage;
    private String hubImage;

}
