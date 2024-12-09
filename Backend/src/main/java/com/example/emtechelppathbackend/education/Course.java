package com.example.emtechelppathbackend.education;

import com.example.emtechelppathbackend.scholars.Scholar;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;


@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "courseCluster", "id", "name", "id" })
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String Name;


    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "course_cluster_id",nullable = false)
    private CourseCluster courseCluster;

//    @ManyToMany(mappedBy = "courses")
//    private Set<Scholar> scholars;
}
