package com.example.emtechelppathbackend.bio;


import org.springframework.data.jpa.repository.JpaRepository;

public interface BioRepository extends JpaRepository<Bio,Long> {
    //retrieve the Bio if you have the userId
    Bio findBioByUserId(Long userId);
}
