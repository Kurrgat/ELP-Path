package com.example.emtechelppathbackend.learning;

import com.example.emtechelppathbackend.utils.CourseLevel;
import com.example.emtechelppathbackend.utils.LearningCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "t_learning")
@Entity
public class LearningEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private LearningCategory category;
    private String courseName;
    private String description;
    @ElementCollection
    @CollectionTable(name = "learning_objectives", joinColumns = @JoinColumn(name = "learning_id"))
    @Column(name = "objective")
    private List<String> objectives;
    private  String image;
    private String link;

    private String document;


}
