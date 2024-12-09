package com.example.emtechelppathbackend.chapter;

import jakarta.persistence.*;

import java.io.Serializable;

@MappedSuperclass
public abstract class RegionOrInstitution implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
}
