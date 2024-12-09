package com.example.emtechelppathbackend.application.wtf_application;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.school.SchoolRepository;
import com.example.emtechelppathbackend.application.schoolhistory.SchoolHistoryRepository;
import com.example.emtechelppathbackend.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final SchoolHistoryRepository schoolHistoryRepository;
    private final SchoolRepository schoolRepository;
	private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Override
    public Application addNewApplication(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public Optional<Application> updateApplicationById(Long id, Application applicationRequest) {
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if (optionalApplication.isPresent()) {
            Application existingApplication = optionalApplication.get();
            existingApplication.setApplicantFirstName(applicationRequest.getApplicantFirstName());
            existingApplication.setApplicantLastName(applicationRequest.getApplicantLastName());
            existingApplication.setApplicationStatus(applicationRequest.getApplicationStatus());
            existingApplication.setDateOfApplication(applicationRequest.getDateOfApplication());
            existingApplication.setBranch(applicationRequest.getBranch());
            existingApplication.setDateOfAwarding(applicationRequest.getDateOfAwarding());
            existingApplication.setDateOfInterview(applicationRequest.getDateOfInterview());
            existingApplication.setScholarCode(applicationRequest.getScholarCode());
            existingApplication.setUser(applicationRequest.getUser());

            // Save the updated Application entity
            applicationRepository.save(existingApplication);

            return Optional.of(existingApplication);
        } else {
            // Handle the case when Application is not present
            // Return an empty Optional, throw an exception, or handle it in another appropriate way
            return Optional.empty();
        }
    }


    @Override
    public void deleteApplicationById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));
        applicationRepository.delete(application);
    }

    @Override
    public List<Application> displayApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public Application displayApplicationDetailsById(Long id) {
        Optional<Application> result = applicationRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("Application", "id", id);
        }
    }

    // Method to return the total number of applications@Override
    @Override
    public long getTotalApplications() {
        String queryString = "SELECT COUNT(a) FROM Application a";
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        return query.getSingleResult();
    }

    // Method to return the number of applications with status "AWARDED"
    @Override
    public long getAwardedApplications() {
        String queryString = "SELECT COUNT (a) FROM Application a WHERE a.applicationStatus = :status";
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("status", ApplicationStatus.AWARDED);
        return query.getSingleResult();
    }

    @Override
    public long getAwaitingApplications() {
        String queryString = "SELECT COUNT (a) FROM Application a WHERE a.applicationStatus = :status";
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("status", ApplicationStatus.AWAITING);
        return query.getSingleResult();
    }

    @Override
    public long getNotAwardedApplications() {
        String queryString = "SELECT COUNT (a) FROM Application a WHERE a.applicationStatus = :status";
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("status", ApplicationStatus.NOT_AWARDED);
        return query.getSingleResult();
    }

    /*
    @Transactional
    public void addSchool(Application application, School school) throws Exception {
        // Check if the application has any school history associated with it
        if (!application.getSchoolHistories().isEmpty()) {
            throw new Exception("School history already exists for the application. Use the transferSchool method instead.");
        }

        // Create a new SchoolHistory entry for the addition of the school
        SchoolHistory newHistory = new SchoolHistory(application, school);
        application.getSchoolHistories().add(newHistory);

        // Set start date and transfer date to the day when the addSchool method was executed
        LocalDate currentDate = LocalDate.now();
        newHistory.setStartDate(currentDate);
        newHistory.setTransferDate(currentDate);

        // Update the application's school field
        application.setSchool(school);

        // Save the changes to Application entity
        applicationRepository.save(application);

        // Save the SchoolHistory entity explicitly
        schoolHistoryRepository.save(newHistory);
    }


    //implement inter school transfers
    @Override
    @Transactional
    public void transferSchool(Long applicationId, Long schoolId) throws Exception {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new Exception("Application not found with ID: " + applicationId));

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new Exception("School not found with ID: " + schoolId));

        // Remove the existing school from the application's history (if it exists)
        application.getSchoolHistories().removeIf(history -> history.getSchool().equals(school));


        // Find the current school history
        SchoolHistory currentHistory = application.getSchoolHistories().stream()
                .filter(history -> history.getEndDate() == null)
                .findFirst()
                .orElseThrow(() -> new Exception("No active school history found for the application."));

        // Set the end date and transfer date for the current school history
        currentHistory.setEndDate(LocalDate.now());
        currentHistory.setTransferDate(LocalDate.now());

        // Create a new SchoolHistory entry for the transfer
        SchoolHistory newHistory = new SchoolHistory(application, school);
        application.getSchoolHistories().add(newHistory);

        // Set start date and transfer date to the day when the transferSchool method was executed
        newHistory.setStartDate(LocalDate.now());
        newHistory.setTransferDate(LocalDate.now());

        // Update the application's school field
        application.setSchool(school);

        // Save the changes to Application entity
        applicationRepository.save(application);

        // Save the changes to SchoolHistory entities
        schoolHistoryRepository.save(currentHistory);
        schoolHistoryRepository.save(newHistory);
    }

    @Override
    @Transactional
    public void endSchool(Long applicationId) throws Exception {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new Exception("Application not found with ID: " + applicationId));

        // Find the current school history
        SchoolHistory currentHistory = application.getSchoolHistories().stream()
                .filter(history -> history.getEndDate() == null)
                .findFirst()
                .orElseThrow(() -> new Exception("No active school history found for the application."));

        // Set the end date to the date when the endSchool method is executed
        currentHistory.setEndDate(LocalDate.now());

        // Save the changes to SchoolHistory entity
        schoolHistoryRepository.save(currentHistory);
    }
*/
    public Map<Integer, Long> getTotalApplicationsByYear() {
        String queryString = "SELECT YEAR(dateOfApplication) as year, COUNT(*) as count " +
                "FROM Application " +
                "GROUP BY YEAR(dateOfApplication)";
        TypedQuery<Object[]> query = entityManager.createQuery(queryString, Object[].class);
        List<Object[]> rows = query.getResultList();

        Map<Integer, Long> results = new HashMap<>();
        for (Object[] row : rows) {
            int year = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            results.put(year, count);
        }

        return results;
    }

    // Method to return the number of applications with status "AWARDED"
    public Map<Integer, Long> getTotalAwardedApplicationsByYear() {
        String queryString = "SELECT YEAR(dateOfAwarding) as year, COUNT(*) as count " +
                "FROM Application " +
                "WHERE applicationStatus = :status " +
                "GROUP BY YEAR(dateOfAwarding)";

        TypedQuery<Object[]> query = entityManager.createQuery(queryString, Object[].class);
        query.setParameter("status", ApplicationStatus.AWARDED);


        List<Object[]> rows = query.getResultList();

        Map<Integer, Long> results = new HashMap<>();
        for (Object[] row : rows) {
            int year = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            results.put(year, count);
        }

        return results;
    }

    @Override
    public Set<ApplicationDto> getApplicationsByBranchId(Long branchId) throws NoResourceFoundException {
        Set<Application> possibleApplicationss = applicationRepository.findApplicationsByBranchId(branchId);
        if (possibleApplicationss.isEmpty()) {
            throw new NoResourceFoundException("This Branch has no applications associated to it");
        } else {
            return possibleApplicationss.stream().map(this::convertAppToDto).collect(Collectors.toSet());
        }
    }
	private ApplicationDto convertAppToDto(Application app){
		return modelMapper.map(app, ApplicationDto.class);
	}
}

