package com.example.emtechelppathbackend.institutionchecker;

import com.example.emtechelppathbackend.chapter.ChapterV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionCheckerRepository extends JpaRepository<InstitutionChecker, Long> {

    Optional<InstitutionChecker> findByNickName(String data);
}
