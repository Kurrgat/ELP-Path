package com.example.emtechelppathbackend.institutionchecker;

import com.example.emtechelppathbackend.chapter.ChapterType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
public class InstitutionChecker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chapterName;
    private String nickName;
    private String chapterDescription;

    @Enumerated(EnumType.STRING)
    private ChapterType chapterType;

}
