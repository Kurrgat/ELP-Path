package com.example.emtechelppathbackend.security.permissions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Permissions {
    //social media endpoint
    SOCIALS_UPDATE("socials:update"),
    SOCIALS_ADD("socials:add"),
    SOCIALS_VIEW("socials:view"),

    FEEDBACK_FILTER_DATE("feedback:filter-date"),
    FEEDBACK_VIEW_ID("feedback:id"),
    FEEDBACK_CREATE("feedback:create"),
    FEEDBACK_VIEW_ALL ("feedback:all"),
    //Skills endpoint

    SKILL_EDIT_TECHNICAL("technical:edit"),
    SKILLS_EDIT_SOFT("soft:edit"),
    SKILL_LANGUAGE_EDIT("language:edit"),
    SKILL_TECHNICAL_ADD("technical:add"),
    SKILL_SOFT_ADD("soft:add"),
    SKILL_LANGUAGE_ADD("language:add"),
    SKILLS_GET_TECHNICAL("technical:get"),
    SKILLS_GET_LANGUAGE("language:get"),
    SKILLS_GET_SOFT("soft:get"),
    SKILL_DELETE("skill:delete"),


    SPOTLIGHT_ALL("spotlight:all"),
    SPOTLIGHT_ID("spotlight:id"),

    //search endpoint
    SEARCH_GET("search:all"),

   //Hubs endpoint
    HUB_APPROVAL("hubs:edit"),


    //school end point
    SCHOOL_UPDATE("school:update"),
    SCHOOL_ADD("school:add"),
    SCHOOL_VIEW("school:view"),
    SCHOOL_APPLICANT_VIEW("school:applicantView"),
    SCHOOL_VIEW_ALL("school:viewAll"),
    SCHOOL_DELETE("school:delete"),

    //scholar-expense endpoint
    SCHOLAR_EXPENSE_UPDATE("scholarExpense:update"),
    SCHOLAR_EXPENSE_ADD("scholarExpense:add"),
    SCHOLAR_EXPENSE_VIEW("scholarExpense:view"),
    SCHOLAR_EXPENSE_VIEW_YEAR_TOTALS("scholarExpense:viewYearTotal"),
    SCHOLAR_EXPENSE_VIEW_STUDENT_TOTAL("scholarExpense:viewStudentTotal"),
    SCHOLAR_EXPENSE_VIEW_GRAND_TOTAL("scholarExpense:viewGrandTotal"),
    SCHOLAR_EXPENSE_DELETE("scholarExpense:delete"),

    //scholar-education endpoint
    SCHOLAR_EDUCATION_UPDATE("scholarEducation:update"),
    SCHOLAR_EDUCATION_ADD("scholarEducation:add"),
    SCHOLAR_EDUCATION_VIEW("scholarEducation:view"),
    SCHOLAR_EDUCATION_DELETE("scholarEducation:delete"),

    //profile endpoint
    PROFILE_UPDATE("profile:update"),
    PROFILE_ADD("profile:add"),
    PROFILE_VIEW("profile:view"),
    PROFILE_VIEW_ALL("profile:viewAll"),
    PROFILE_DELETE("profile:delete"),

    //feeds endpoint
    FEEDS_UPDATE("feeds:update"),
    FEEDS_ADD("feeds:add"),
    FEEDS_VIEW("feeds:view"),
    FEEDS_USER_VIEW("feeds:userView"),
    FEEDS_VIEW_ALL("feeds:viewAll"),
    FEEDS_DELETE("feeds:delete"),

    //events endpoint
    EVENTS_UPDATE("events:update"),
    EVENTS_PARTICIPATE("events:participate"),
    EVENTS_CANCEL_PARTICIPATION("events:cancelParticipation"),
    EVENTS_CHAPTER_ADD("events:chapterAdd"),
    EVENTS_ADD("events:add"),
    EVENTS_VIEW("events:view"),
    EVENTS_DATE_VIEW("events:dateView"),
    EVENTS_DATE_COUNT("events:dateCount"),
    EVENTS_CHAPTER_VIEW("events:chapterView"),
    EVENTS_CHAPTER_COUNT("events:chapterCount"),
    EVENTS_ACTIVE_VIEW("events:activeView"),
    EVENTS_ACTIVE_COUNT("events:activeCount"),
    EVENTS_PAST_VIEW("events:pastView"),
    EVENTS_PAST_COUNT("events:pastCount"),
    EVENTS_SCHEDULED_VIEW("events:scheduledView"),
    EVENTS_SCHEDULED_COUNT("events:scheduledCount"),
    EVENTS_VIEW_ALL("events:allView"),
    EVENTS_COUNT_ALL("events:countAll"),
    EVENTS_DELETE("events:delete"),
    APPROVE_CHAPTER_EVENTS("events:edit"),
    EVENTS_APPROVAL_VIEW("events:get"),
    APPROVE_HUB_EVENTS("events:edit"),

    //elp-endpoint
    ELPS_UPDATE("elps:update"),
    ELPS_MAKE("elps:make"),
    ELPS_CREATE("elps:create"),
    ELPS_VIEW("elps:view"),
    ELPS_WTFS_VIEW("elps:wtfsView"),
    ELPS_VIEW_ALL("elps:viewAll"),

    //education endpoint
    EDUCATION_UPDATE("education:update"),
    EDUCATION_ADD("education:add"),
    EDUCATION_USER_VIEW("education:userView"),
    EDUCATION_DELETE("education:delete"),

    //chapter controller
    CHAPTER_UPDATE("chapter:update"),
    CHAPTER_LEAVE("chapter:leave"),
    CHAPTER_JOIN("chapter:join"),
    CHAPTER_ADD("chapter:add"),
    CHAPTER_VIEW("chapter:view"),
    CHAPTER_MEMBERS_VIEW("chapter:membersView"),
    CHAPTER_VIEW_ALL("chapter:viewAll"),
    CHAPTER_DELETE("chapter:delete"),

    //chapter-type endpoint
    CHAPTER_TYPE_UPDATE("chapterType:update"),
    CHAPTER_TYPE_ADD("chapterType:add"),
    CHAPTER_TYPE_VIEW("chapterType:view"),
    CHAPTER_TYPE_VIEW_ALL("chapterType:viewAll"),
    CHAPTER_TYPE_DELETE("chapterType:delete"),

    //career endpoint
    CAREER_UPDATE("career:update"),
    CAREER_ADD("career:add"),
    CAREER_VIEW("career:view"),
    CAREER_DELETE("career:delete"),

    //branch endpoint
    BRANCH_UPDATE("branch:update"),
    BRANCH_ADD("branch:add"),
    BRANCH_VIEW("branch:view"),
    BRANCH_VIEW_ALL("branch:viewAll"),
    BRANCH_DELETE("branch:delete"),

    //applications endpoint
    APPLICATIONS_UPDATE("applications:update"),
    APPLICATIONS_ADD_SCHOOL("applications:addSchool"),
    APPLICATIONS_TRANSFER_SCHOOL("applications:transferSchool"),
    APPLICATIONS_END_SCHOOL("applications:endSchool"),
    APPLICATIONS_ADD("applications:add"),
    APPLICATIONS_BRANCH_VIEW("applications:branchView"),
    APPLICATIONS_VIEW_ALL("applications:viewAll"),
    APPLICATIONS_VIEW("applications:view"),
    APPLICATIONS_NOT_AWARDED_COUNT("applications:notAwardedCount"),
    APPLICATIONS_AWARDED_COUNT("applications:awardedCount"),
    APPLICATIONS_YEAR_AWARDED_COUNT("applications:yearAwardedCount"),
    APPLICATIONS_AWAITING_COUNT("applications:awaitingCount"),
    APPLICATIONS_YEAR_COUNT("applications:yearCount"),
    APPLICATIONS_ALL_COUNT("applications:allCount"),
    APPLICATIONS_DELETE("applications:delete"),



    //roles endpoint
    ROLES_UPDATE("roles:update"),
    ROLES_ADD("roles:add"),
    ROLES_VIEW_ALL("roles:viewAll"),
    ROLES_VIEW("roles:view"),
    ROLES_DELETE("roles:delete"),

    //emails endpoint
    EMAILS_SEND("emails:send"),

    //bio endpoint
    BIO_UPDATE("bio:update"),
    BIO_ADD("bio:add"),
    BIO_VIEW("bio:view"),
    BIO_DELETE("bio:delete"),

    //school-histories endpoint
    SCHOOL_HISTORIES_VIEW("schoolHistories:view"),

    //users endpoint
    USER_ROLE_UPDATE("roles:user_role_update"),
    USER_VIEW_ALL("user:viewAll"),
    USER_COUNT_ALL("user:countAll"),
    USER_DELETE("user:delete"),

    //registering a new admin
    ADD_NEW_ADMIN("registeringNewAdmin"),

    ORGANIZATION_ADD("organization:add"),
    ORGANIZATION_VIEW("organization:view"),
    ORGANIZATION_UPDATE("organization:update"),
    ORGANIZATION_DELETE("organization:delete"),

    JOB_OPPORTUNITY_ADD("jobOpportunity:add"),
    JOB_OPPORTUNITY_UPDATE("jobOpportunity:update"),
    JOB_OPPORTUNITY_VIEW("jobOpportunity:view"),
    JOB_OPPORTUNITY_DELETE("jobOpportunity:delete");
    private final String permission;
}