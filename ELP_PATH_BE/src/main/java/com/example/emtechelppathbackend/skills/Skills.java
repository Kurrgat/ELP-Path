package com.example.emtechelppathbackend.skills;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.LanguageLevel;
import com.example.emtechelppathbackend.utils.SoftSkillsLevel;
import com.example.emtechelppathbackend.utils.TechnicalLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_skills")
public class Skills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skillsName;
    @Enumerated(EnumType.STRING)
    @Column(name = "technical_level")
    private TechnicalLevel technicalLevel;
    @Enumerated(EnumType.STRING)
    @Column(name = "language_level")
    private LanguageLevel languageLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "soft_skills_level")
    private SoftSkillsLevel softSkillsLevel;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users users;

}
