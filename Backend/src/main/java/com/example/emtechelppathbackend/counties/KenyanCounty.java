package com.example.emtechelppathbackend.counties;

import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.scholars.Scholar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KenyanCounty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Long Code;
    private String Name;
    private String FormerProvince;
    private String PostalAbbreviation;

    @ManyToOne(cascade = CascadeType.ALL)
    private ChapterV2 chapterV2;

}
