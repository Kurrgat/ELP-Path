package com.example.emtechelppathbackend.scholars;

public interface ScholarDTO_2 {
    long id = 0;
    String pfNumber = null;
    String scholarCode = null;
    String scholarFirstName = null;
    String scholarLastName = null;
    //@JsonFormat(pattern = "yyyy-MM-dd")
    //private String scholarDOB;
    String gender = null;
    String branch = null;
    String scholarCategory = null;
    String yearOfJoiningHighSchoolProgram = null;
    String yearOfJoiningTertiaryProgram = null;
    String school = null;
    String institution = null;
    String donor = null;
    String homeCounty = null;
    String scholarType = null;
    String nationality = null;

    long getId();

    String getPfNumber();

    String getScholarCode();

    String getScholarFirstName();

    String getScholarLastName();

    String getGender();

    String getBranch();

    String getScholarCategory();

    String getYearOfJoiningHighSchoolProgram();

    String getYearOfJoiningTertiaryProgram();

    String getSchool();

    String getInstitution();

    String getDonor();

    String getHomeCounty();

    String getScholarType();

    String getNationality();
}
