package com.example.emtechelppathbackend.chapter;

public interface ChapterInterface {
    Long getChapter_Id();
    ChapterType getChapter_type();
    String getDescription();
    String getChapterName();
    String getImage_url();
    Long getChapter_leader_id();
    Long getScholar_id();
    String getScholar_category();
    String getScholar_first_name();
    Long getScholar_branch();
    Long getInstitution_id();
    String getInstitution_category();
    String getInstitution_name();
    Long getUser_id();
    String getFirst_name();
    String getUsername();
}
