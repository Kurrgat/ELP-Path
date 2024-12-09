package com.example.emtechelppathbackend.education;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CourseCluster  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(nullable = false)
    private String name;


    @OneToMany(mappedBy = "courseCluster")
    @JsonIgnore
    @ToString.Exclude
    private List<Course> courses;
}

