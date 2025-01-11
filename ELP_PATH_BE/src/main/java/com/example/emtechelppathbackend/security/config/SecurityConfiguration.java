package com.example.emtechelppathbackend.security.config;

import com.example.emtechelppathbackend.security.jwtservices.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import static com.example.emtechelppathbackend.security.permissions.Permissions.*;
import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.csrf(csrf -> csrf.disable())//disabling csfr
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        //non authorization requiring requests
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/auth/register","/auth/authenticate", "/auth/confirm/scholar-email",  "/join-request/**", "/reset-password/**","/v3/api-docs/**", "/swagger-ui/**", "/images/**").permitAll()
                        .requestMatchers(OPTIONS).permitAll()

                        //securing adding an admin
                        .requestMatchers("/auth/register-admin").hasAuthority(ADD_NEW_ADMIN.name())

                        //securing social media endpoint
                        .requestMatchers(PUT, "/socials/{userId}/update").hasAuthority(SOCIALS_UPDATE.name())
                        .requestMatchers(POST, "/socials/{userId}/add").hasAuthority(SOCIALS_ADD.name())
                        .requestMatchers(GET, "/socials/{userId}/view").hasAuthority(SOCIALS_VIEW.name())

                        //securing school endpoint
                        .requestMatchers(PUT, "/schools/update/{id}").hasAuthority(SCHOOL_UPDATE.name())
                        .requestMatchers(POST, "/schools/add-new-school").hasAuthority(SCHOOL_ADD.name())
                        .requestMatchers(GET, "/schools/{applicationId}/display-school").hasAuthority(SCHOOL_APPLICANT_VIEW.name())
                        .requestMatchers(GET, "/schools/display/{id}").hasAuthority(SCHOOL_VIEW.name())
//                        .requestMatchers(GET, "/schools/all").hasAuthority(SCHOOL_VIEW_ALL.name())
                                .requestMatchers(GET, "/schools/all").permitAll()
                        .requestMatchers(DELETE, "/schools/delete/{id}").hasAuthority(SCHOOL_DELETE.name())

                        //securing scholar-expense endpoint
                        .requestMatchers(PUT, "/scholar/expenses/update/{scholarExpensesId}").hasAuthority(SCHOLAR_EXPENSE_UPDATE.name())
                        .requestMatchers(POST, "/scholar/expenses/{scholarId}/add").hasAuthority(SCHOLAR_EXPENSE_ADD.name())
                        .requestMatchers(GET, "/scholar/expenses/{scholarId}/view").hasAuthority(SCHOLAR_EXPENSE_VIEW.name())
                        .requestMatchers(GET, "/scholar/expenses/total_expense_per_year").hasAuthority(SCHOLAR_EXPENSE_VIEW_YEAR_TOTALS.name())
                        .requestMatchers(GET, "/scholar/expenses/student-total-expense/{applicationId}").hasAuthority(SCHOLAR_EXPENSE_VIEW_STUDENT_TOTAL.name())
                        .requestMatchers(GET, "/scholar/expenses/grand-totals").hasAuthority(SCHOLAR_EXPENSE_VIEW_GRAND_TOTAL.name())
                        .requestMatchers(DELETE, "/scholar/expenses/delete/{scholarExpenseId}").hasAuthority(SCHOLAR_EXPENSE_DELETE.name())

                        //securing scholar-education endpoint
                        .requestMatchers(PUT, "/scholar/education/update/{scholarEducationId}").hasAuthority(SCHOLAR_EDUCATION_UPDATE.name())
                        .requestMatchers(POST, "/scholar/education/{scholarId}/add").hasAuthority(SCHOLAR_EDUCATION_ADD.name())
                        .requestMatchers(GET, "/scholar/education/{scholarId}/view").hasAuthority(SCHOLAR_EDUCATION_VIEW.name())
                        .requestMatchers(DELETE, "/scholar/education/delete/{scholarEducationId}").hasAuthority(SCHOLAR_EDUCATION_DELETE.name())




                        .requestMatchers(GET, "/ privacy/{userId}/view").permitAll()
                        .requestMatchers(POST, "/privacy/add/{userId}").permitAll()
                                .requestMatchers(PUT, " /privacy/update/{userId}").permitAll()

                //securing profile endpoint


                        .requestMatchers(GET, "/profile/{profileId}").permitAll()
                        .requestMatchers(PUT, "/profile/{profileId}/update").permitAll()
                        .requestMatchers(POST, "/profile/{userId}/create").permitAll()
                        .requestMatchers(GET, "/{profileId}public-view").permitAll()
                        .requestMatchers(PUT, "/profile/{profileId}/edit").permitAll()
                                .requestMatchers(PUT, "/profile/{profileId}/update-image").hasAuthority(PROFILE_UPDATE.name())

                        //comments endpoint
                         .requestMatchers(POST, "/comments/add/{userId}/{feedId}").permitAll()
                        .requestMatchers(GET, "/comments/total/{feedId}").permitAll()
                                .requestMatchers(GET, "/comments/all/{feedId}").permitAll()


                        // likes endpoint
                                .requestMatchers(POST, "/like/add/{userId}/{feedId}").permitAll()
                                .requestMatchers(GET, "/like/total/{feedId}").permitAll()
                                .requestMatchers(GET, "/like/all/{feedId}").permitAll()
                                .requestMatchers(DELETE, "/like/{likeId}").permitAll()






                        //securing profile endpoint


                        .requestMatchers(GET, "/profile/{profileId}").permitAll()
                        .requestMatchers(PUT, "/profile/{profileId}/update").permitAll()
                        .requestMatchers(POST, "/profile/{userId}/create").permitAll()
                        .requestMatchers(GET, "/{profileId}public-view").permitAll()
                        .requestMatchers(PUT, "/profile/{profileId}/edit").permitAll()

                        .requestMatchers(GET, "/profile/{userId}/view").permitAll()
                        .requestMatchers(GET, "/profile/all").hasAuthority(PROFILE_VIEW_ALL.name())
                        .requestMatchers(DELETE, "/profile/{userId}/delete").hasAuthority(PROFILE_DELETE.name())


                        .requestMatchers(PUT, "/v2/feeds/{id}/edit").permitAll()
                        .requestMatchers(POST, "/v2/feeds/{userId}/add").permitAll()
                        .requestMatchers(GET, "/v2/feeds/{id}").permitAll()
                        .requestMatchers(GET, "/v2/feeds/{id}/view").permitAll()
                        .requestMatchers(GET, "/v2/feeds/all").permitAll()
                        .requestMatchers(GET, "/v2/feeds/images").permitAll()
                        .requestMatchers(DELETE, "/v2/feeds/{id}/delete").permitAll()

                        //securing events endpoint
                        .requestMatchers(PUT, "/events//{eventId}").hasAuthority(EVENTS_UPDATE.name())
                        .requestMatchers(POST, "/events/{updateuserId}/participate{eventId}").hasAuthority(EVENTS_PARTICIPATE.name())
                        .requestMatchers(POST, "/events/{userId}/cancel-participation{eventId}").hasAuthority(EVENTS_CANCEL_PARTICIPATION.name())
                        .requestMatchers(POST, "/events/{chapterId}/create-chapter-event").hasAuthority(EVENTS_CHAPTER_ADD.name())
                        .requestMatchers(POST, "/events/create-event").hasAuthority(EVENTS_ADD.name())
                        .requestMatchers(GET, "/events/{eventId}/display").hasAuthority(EVENTS_VIEW.name())
                        .requestMatchers(GET, "/events/{eventDate}/display-events-by-date").hasAuthority(EVENTS_DATE_VIEW.name())
                        .requestMatchers(GET, "/events/{eventDate}/count-events-by-date").hasAuthority(EVENTS_DATE_COUNT.name())
                        .requestMatchers(GET, "/events/{chapterId}/display-chapter-events").hasAuthority(EVENTS_CHAPTER_VIEW.name())
                        .requestMatchers(GET, "/events/{chapterId}/count-chapter-events").hasAuthority(EVENTS_CHAPTER_COUNT.name())
                        .requestMatchers(GET, "/events/display-scheduled-events").hasAuthority(EVENTS_SCHEDULED_VIEW.name())
                        .requestMatchers(GET, "/events/display-past-events").hasAuthority(EVENTS_PAST_VIEW.name())
                        .requestMatchers(GET, "/events/display-active-events").hasAuthority(EVENTS_ACTIVE_VIEW.name())
                        .requestMatchers(GET, "/events/count-scheduled-events").hasAuthority(EVENTS_SCHEDULED_COUNT.name())
                        .requestMatchers(GET, "/events/count-past-events").hasAuthority(EVENTS_PAST_COUNT.name())
                        .requestMatchers(GET, "/events/count-all-events").hasAuthority(EVENTS_COUNT_ALL.name())
                        .requestMatchers(GET, "/events/count-active-events").hasAuthority(EVENTS_ACTIVE_COUNT.name())
                        .requestMatchers(GET, "/events/all").hasAuthority(EVENTS_VIEW_ALL.name())
                        .requestMatchers(DELETE, "/events/{eventId}/delete").hasAuthority(EVENTS_DELETE.name())
                                .requestMatchers(GET, "get-events-for-approval").hasAuthority(EVENTS_APPROVAL_VIEW.name())

                        //securing ELP endpoint
                        .requestMatchers(PUT, "/elps/{elpId}/update").hasAuthority(ELPS_UPDATE.name())
                        .requestMatchers(POST, "/elps/{applicationId}/make-elp").hasAuthority(ELPS_MAKE.name())
                        .requestMatchers(POST, "/elps/create-elp").hasAuthority(ELPS_CREATE.name())
                        .requestMatchers(GET, "/elps/{elpId}/view").hasAuthority(ELPS_VIEW.name())
                        .requestMatchers(GET, "/elps/elps-who-were-wtfs").hasAuthority(ELPS_WTFS_VIEW.name())
                        .requestMatchers(GET, "/elps/all").hasAuthority(ELPS_VIEW_ALL.name())

                        //securing Education endpoint
                        .requestMatchers(PUT, "/education/{userId}/{educationId}/update").hasAuthority(EDUCATION_UPDATE.name())
                        .requestMatchers(POST, "/education/{userId}/create").hasAuthority(EDUCATION_ADD.name())
                        .requestMatchers(GET, "/education/{userId}/view").hasAuthority(EDUCATION_USER_VIEW.name())
                        .requestMatchers(DELETE, "/education/{userId}/{educationId}/delete").hasAuthority(EDUCATION_DELETE.name())

                        //securing chapter endpoint
                        .requestMatchers(PUT, "/chapters/{chapterId}/update").hasAuthority(CHAPTER_UPDATE.name())
                        .requestMatchers(POST, "/chapters/{userId}/{chapterId}/leave").hasAuthority(CHAPTER_LEAVE.name())
                        .requestMatchers(POST, "/chapters/{userId}/{chapterId}/join").hasAuthority(CHAPTER_JOIN.name())
                        .requestMatchers(POST, "/chapters/create").hasAuthority(CHAPTER_ADD.name())
                        .requestMatchers(GET, "/chapters/{chapterId}/view").hasAuthority(CHAPTER_VIEW.name())
                        .requestMatchers(GET, "/chapters/{chapterId}/display-chapter-members").hasAuthority(CHAPTER_MEMBERS_VIEW.name())
                        .requestMatchers(GET, "/chapters/all").hasAuthority(CHAPTER_VIEW_ALL.name())
                        .requestMatchers(DELETE, "/chapters/{chapterId}/delete").hasAuthority(CHAPTER_DELETE.name())

                        //securing chapter types endpoint
                        .requestMatchers(PUT, "/chapter-types/{chapterTypeId}/update").hasAuthority(CHAPTER_TYPE_UPDATE.name())
                        .requestMatchers(POST, "/chapter-types/add-new-ChapterType").hasAuthority(CHAPTER_TYPE_ADD.name())
                        .requestMatchers(GET, "/chapter-types/{ChapterTypeId}/display").hasAuthority(CHAPTER_TYPE_VIEW.name())
                        .requestMatchers(GET, "/chapter-types/all").hasAuthority(CHAPTER_TYPE_VIEW_ALL.name())
                        .requestMatchers(DELETE, "/chapter-types/{chapterTypeId}/delete").hasAuthority(CHAPTER_TYPE_DELETE.name())

                        //securing career endpoint
                        .requestMatchers(PUT, "/career/user/{userId}/{careerId}/update").hasAuthority(CAREER_UPDATE.name())
                        .requestMatchers(POST, "/career/{userId}/create").hasAuthority(CAREER_ADD.name())
                        .requestMatchers(GET, "/career/{userId}/view").hasAuthority(CAREER_VIEW.name())
                        .requestMatchers(DELETE, "/career/{careerId}/delete").hasAuthority(CAREER_DELETE.name())

                        //securing branch endpoint
                        .requestMatchers(PUT, "/branch/update/{id}").hasAuthority(BRANCH_UPDATE.name())
                        .requestMatchers(POST, "/branch/add-new-branch").hasAuthority(BRANCH_ADD.name())
                        .requestMatchers(POST, "/branch/add-new-branch").permitAll()
                        .requestMatchers(GET, "/branch/display/{id}").hasAuthority(BRANCH_VIEW.name())
                        .requestMatchers(GET, "/branch/all").permitAll()
                        .requestMatchers(DELETE, "/branch/delete/{id}").hasAuthority(BRANCH_DELETE.name())

                        //securing bio endpoint
                        .requestMatchers(PUT, "/bio/{userId}/{bioId}/update").hasAuthority(BIO_UPDATE.name())
                        .requestMatchers(POST, "/bio/{userId}/add").hasAuthority(BIO_ADD.name())
                        .requestMatchers(GET, "/bio/{userId}/view").hasAuthority(BIO_VIEW.name())
                        .requestMatchers(DELETE, "/bio/{userId}/{bioId}/delete").hasAuthority(BIO_DELETE.name())

                        //securing applications/scholars endpoint
                        .requestMatchers(PUT, "/applications/update-application/{id}").hasAuthority(APPLICATIONS_UPDATE.name())
                        .requestMatchers(POST, "/applications/{applicationId}/add-school/{schoolId}").hasAuthority(APPLICATIONS_ADD_SCHOOL.name())
                        .requestMatchers(POST, "/applications/transfer-school").hasAuthority(APPLICATIONS_TRANSFER_SCHOOL.name())
                        .requestMatchers(POST, "/applications/end-school").hasAuthority(APPLICATIONS_END_SCHOOL.name())
                        .requestMatchers(POST, "/applications/add-new-application").hasAuthority(APPLICATIONS_ADD.name())
                        .requestMatchers(GET, "/applications/{branchId}/display-applications").hasAuthority(APPLICATIONS_BRANCH_VIEW.name())
                        .requestMatchers(GET, "/applications/display-applications").hasAuthority(APPLICATIONS_VIEW_ALL.name())
                        .requestMatchers(GET, "/applications/display-applications/{id}").hasAuthority(APPLICATIONS_VIEW.name())
                        .requestMatchers(GET, "/applications/count-not-awarded-applications").hasAuthority(APPLICATIONS_NOT_AWARDED_COUNT.name())
                        .requestMatchers(GET, "/applications/count-awarded-applications").hasAuthority(APPLICATIONS_AWARDED_COUNT.name())
                        .requestMatchers(GET, "/applications/count-awarded-applications-by-year").hasAuthority(APPLICATIONS_YEAR_AWARDED_COUNT.name())
                        .requestMatchers(GET, "/applications/count-awaiting-applications").hasAuthority(APPLICATIONS_AWAITING_COUNT.name())
                        .requestMatchers(GET, "/applications/count-applications-by-year").hasAuthority(APPLICATIONS_YEAR_COUNT.name())
                        .requestMatchers(GET, "/applications/count-all-applications").hasAuthority(APPLICATIONS_ALL_COUNT.name())
                        .requestMatchers(DELETE, "/applications/delete-application/{id}").hasAuthority(APPLICATIONS_DELETE.name())



                        .requestMatchers(POST, "/com/example/emtechelppathbackend/feedback/create-feedback").hasAuthority(FEEDBACK_CREATE.name())
                        .requestMatchers(GET, "/com/example/emtechelppathbackend/feedback/all").hasAuthority(FEEDBACK_VIEW_ALL.name())
                        .requestMatchers(GET, "/com/example/emtechelppathbackend/feedback/all").hasAuthority(FEEDBACK_FILTER_DATE.name())
                        .requestMatchers(GET, "/com/example/emtechelppathbackend/feedback/all").hasAuthority(FEEDBACK_VIEW_ID.name())



                        // search endpoint
                        .requestMatchers(GET, "/search").permitAll()

                        //reward endpoint
                                .requestMatchers(POST, " /reward/create").permitAll()
                                .requestMatchers(POST, "    /userPoints/award").permitAll()


                        //securing newsLetter endpoint
                                .requestMatchers(POST, "/NewsLetter/request-newsletter/{email}").permitAll()

                        //securing news and updates endpoint
                                .requestMatchers(GET, "/newsAndUpdates/get-all").permitAll()
                                .requestMatchers(GET, "/newsAndUpdates/get/{id}").permitAll()

                                //SportLight endpoint
                        .requestMatchers(POST, "/spotlight/spotlight-create").permitAll()
                        .requestMatchers(PUT,"/spotlight/{id}/spotlight").permitAll()
                        .requestMatchers(GET, "/spotlight/spotlight-all").permitAll()
                        .requestMatchers(GET, "/spotlight/{id}/spotlight").permitAll()
                         //email otp endpoint
                        .requestMatchers(POST, "/generate-otp").permitAll()
                        .requestMatchers(POST, "/verify-otp").permitAll()


                        //survey Endpoint
                        .requestMatchers(POST, " /survey/forChapter/{chapterId}").permitAll()
                        .requestMatchers(POST, " /survey/forHub/{hubId}").permitAll()
                        .requestMatchers(GET, " /survey/hub/{hubId}").permitAll()
                        .requestMatchers(GET, " /survey/chapter/{chapterId}").permitAll()
                        .requestMatchers(GET, "  /survey/institutional").permitAll()
                        .requestMatchers(GET, "  /survey/regional").permitAll()








                        // .requestMatchers(POST, "/roles/add-new-role").hasAuthority(ROLES_ADD.name())
                        .requestMatchers(PUT,"/roles/{roleId}/update").hasAuthority(ROLES_UPDATE.name())
                        .requestMatchers(POST, "/roles/add-new-role").permitAll()
                        // .requestMatchers(GET, "/roles/{roleId}/view").hasAuthority(ROLES_VIEW.name())
                        .requestMatchers(GET, "/roles/{roleId}/view", "/roles/all").permitAll()
                        .requestMatchers(DELETE,"/roles/{roleId}/delete").hasAuthority(ROLES_DELETE.name())

                        //securing users endpoint


                        .requestMatchers(PUT, "/users/update-user-role/{userId}/{roleName}").hasAuthority(USER_ROLE_UPDATE.name())
                        .requestMatchers(GET,"/users/**").hasAuthority(USER_VIEW_ALL.name())
                        .requestMatchers(GET,"/users/count-all-users").hasAuthority(USER_COUNT_ALL.name())
                        .requestMatchers(DELETE, "/users/{userId}/delete").hasAuthority(USER_DELETE.name())

                        //securing emails endpoint
                        .requestMatchers(POST, "/emails/**").hasAuthority(EMAILS_SEND.name())

                        //school history endpoint
                        .requestMatchers(GET, "/school-history/{applicationId}/display").hasAuthority(SCHOOL_HISTORIES_VIEW.name())

                        //securing organization endpoint
                        .requestMatchers(PUT, "/organization/{organizationId}/update").hasAuthority(ORGANIZATION_UPDATE.name())
                        .requestMatchers(POST,"/organization/create").hasAuthority(ORGANIZATION_ADD.name())
                        .requestMatchers(GET,"/organization/{organizationId}/view", "/organization/all").hasAuthority(ORGANIZATION_VIEW.name())
                        .requestMatchers(DELETE,"/organization/{organizationId}/delete").hasAuthority(ORGANIZATION_DELETE.name())


                        //job opportunities endpoint
                        .requestMatchers(PUT,"/opportunities/{jobId}/update").hasAuthority(JOB_OPPORTUNITY_UPDATE.name())
                        // .requestMatchers(GET,"/opportunities/{organizationId}/view-by-organization","/opportunities/{jobId}/view",
                        //       "/opportunities/view-active","/opportunities/count-all", "/opportunities/count-active", "/opportunities/all").hasAuthority(JOB_OPPORTUNITY_VIEW.name())
                        .requestMatchers(GET,"/opportunities/{organizationId}/view-by-organization","/opportunities/{jobId}/view",
                                "/opportunities/view-active","/opportunities/count-all", "/opportunities/count-active", "/opportunities/all").permitAll()
                        .requestMatchers(POST,"/opportunities/job/create","/opportunities/job/create-with-poster").hasAuthority(JOB_OPPORTUNITY_ADD.name())
                        // .requestMatchers(POST,"/opportunities/job/create","/opportunities/job/create-with-poster").permitAll()
                        .requestMatchers(DELETE, "/opportunities/{jobId}/delete").hasAuthority(JOB_OPPORTUNITY_DELETE.name())


                        .anyRequest().authenticated()// Require authentication for other APIs
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }


    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

}