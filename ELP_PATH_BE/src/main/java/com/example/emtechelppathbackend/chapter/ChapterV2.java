package com.example.emtechelppathbackend.chapter;

import com.example.emtechelppathbackend.counties.KenyanCounty;
import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.security.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ChapterV2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chapterName;
    private String nickName;
    private String chapterImage;
    @Length(max = 10000)
    private String chapterDescription;

    @Enumerated(EnumType.STRING)
    private ChapterType chapterType;


    private Long regionOrInstitutionId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Users chapterLeader;




}
