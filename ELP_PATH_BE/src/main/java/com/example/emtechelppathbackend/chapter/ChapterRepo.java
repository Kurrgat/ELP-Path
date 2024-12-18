package com.example.emtechelppathbackend.chapter;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepo extends JpaRepository<Chapter,Long> {
//	  List<Users> findMembersByChapterId(Long chapter_Id);
}
