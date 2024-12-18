package com.example.emtechelppathbackend.chapter;

//import com.example.emtechelppathbackend.counties.KenyanCounty;
import com.example.emtechelppathbackend.education.Institution;
import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.security.user.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class ChapterHandler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Boolean isInstitutionalChapterLeader;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Boolean isRegionalChapterLeader;

    @ManyToOne(cascade = CascadeType.ALL)
    private Institution institution;


    @OneToOne(cascade = CascadeType.ALL)
    private Users users;

}
