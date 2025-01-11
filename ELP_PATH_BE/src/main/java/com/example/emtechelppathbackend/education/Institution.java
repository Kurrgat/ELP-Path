package com.example.emtechelppathbackend.education;

import com.example.emtechelppathbackend.country.Country;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String CountryCode;
    private String Name;
    private String Website;
    private String Category;

    @ManyToOne
    private Country country;
}
