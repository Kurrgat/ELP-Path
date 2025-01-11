package com.example.emtechelppathbackend.chapter.chapterv2;

//import com.example.emtechelppathbackend.counties.KenyanCounty;
import com.example.emtechelppathbackend.education.Institution;
import com.example.emtechelppathbackend.scholars.Scholar;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class ChapterHandlerv2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Boolean isInstitutionalChapterLeader;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Boolean isRegionalChapterLeader;

    @ManyToOne(cascade = CascadeType.ALL)
    private Institution institution;

   // @OneToOne(cascade = CascadeType.ALL)
    //private KenyanCounty HomeCounty;

    //@OneToOne(cascade = CascadeType.ALL)
    //private KenyanCounty CountyOfResidence;

    @OneToOne(cascade = CascadeType.ALL)
    private Scholar scholar;

}
