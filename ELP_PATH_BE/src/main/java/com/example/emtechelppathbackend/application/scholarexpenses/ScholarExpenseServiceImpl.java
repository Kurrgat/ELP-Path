package com.example.emtechelppathbackend.application.scholarexpenses;

import com.example.emtechelppathbackend.application.wtf_application.Application;
import com.example.emtechelppathbackend.application.wtf_application.ApplicationRepository;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScholarExpenseServiceImpl implements ScholarExpenseService {
	  private final ApplicationRepository applicationRepository;
	  private final ScholarExpenseRepo scholarExpenseRepo;
	  private final EntityManager entityManager;


	  @Override
	  public ScholarExpenseDto addScholarExpenses(Long scholarId, ScholarExpenseDto scholarExpenseDto) {
		    ScholarExpenses scholarExpenses = mapToEntity(scholarExpenseDto);
		    Application application = applicationRepository.findById(scholarId).orElseThrow(() -> new UserDetailsNotFoundException("applicant not found"));
		    /* scholarExpenses.setScholar(scholar);*/
		    scholarExpenses.setApplication(application);
		    scholarExpenses.setId(scholarExpenseDto.getId());
                scholarExpenses.setSchool(scholarExpenseDto.getSchool());
		    scholarExpenses.setForm(scholarExpenseDto.getForm());
		    scholarExpenses.setTerm(scholarExpenseDto.getTerm());
		    scholarExpenses.setStart_date(scholarExpenseDto.getStart_date());
		    scholarExpenses.setShopping(scholarExpenseDto.getShopping());
		    scholarExpenses.setSchoolFees(scholarExpenseDto.getSchoolFees());
		    scholarExpenses.setTransport(scholarExpenseDto.getTransport());
		    scholarExpenses.setUpkeep(scholarExpenseDto.getUpkeep());
		    //Integer total = scholarExpenseRepo.findAll().stream().map(ScholarExpenses::getTotal).sum();
		    scholarExpenses.setScholarTotalPerTerm(scholarExpenseDto.getTotal());
		    //scholarExpenses.setApplication(scholarExpenseDto.getApplication());
		    ScholarExpenses newScholarExpenses = scholarExpenseRepo.save(scholarExpenses);
		    return mapToDto(newScholarExpenses);
	  }

	  @Override
	  public List<ScholarExpenseDto> findExpensesByApplicationId(Long applicationId) {
		    List<ScholarExpenses> scholarExpenses = scholarExpenseRepo.findByApplicationId(applicationId);
		    return scholarExpenses.stream().map(this::mapToDto).collect(Collectors.toList());
	  }

	  @Override
	  public ScholarExpenseDto updateScholarExpenses(Long scholarExpensesId, ScholarExpenseDto updatedScholarExpenseDto) {
		    ScholarExpenses oldScholarExpenses = scholarExpenseRepo.findById(scholarExpensesId)
				.orElseThrow(() -> new NoResourceFoundException("No scholar expenses found"));// why not reuse a custom exception?

		    oldScholarExpenses.setForm(updatedScholarExpenseDto.getForm());
		    oldScholarExpenses.setTerm(updatedScholarExpenseDto.getTerm());
//        oldScholarExpenses.setSchoolName(updatedScholarExpenseDto.getSchoolName());
		    oldScholarExpenses.setStart_date(updatedScholarExpenseDto.getStart_date());
		    oldScholarExpenses.setSchoolFees(updatedScholarExpenseDto.getSchoolFees());
		    oldScholarExpenses.setShopping(updatedScholarExpenseDto.getShopping());
		    oldScholarExpenses.setTransport(updatedScholarExpenseDto.getTransport());
		    oldScholarExpenses.setUpkeep(updatedScholarExpenseDto.getUpkeep());
		    oldScholarExpenses.setScholarTotalPerTerm(updatedScholarExpenseDto.getTotal());

		    ScholarExpenses updatedScholarExpenses = scholarExpenseRepo.save(oldScholarExpenses);

		    return mapToDto(updatedScholarExpenses);
	  }

	  @Override
	  public void deleteScholarExpenseById(Long scholarExpenseId) {
		    if (!scholarExpenseRepo.existsById(scholarExpenseId)) {
				throw new NoResourceFoundException("No scholar education found for ID: " + scholarExpenseId);
		    }
		    scholarExpenseRepo.deleteById(scholarExpenseId);
	  }

	  // Get the total expenditure per scholar
	  public int getTotalExpenditurePerScholar(Long applicationId) {
		    List<ScholarExpenses> expensesList = scholarExpenseRepo.findByApplicationId(applicationId);
		    int totalExpenditurePerScholar = 0;
		    for (ScholarExpenses expenses : expensesList) {
				totalExpenditurePerScholar += expenses.getScholarTotalPerTerm();
		    }
		    return totalExpenditurePerScholar;
	  }

	@Override
	public Map<Integer, Integer> getTotalExpenseByYear() {
		String queryString = "SELECT YEAR(start_date) as year, SUM(schoolFees + shopping + transport + upkeep) as total " +
				"FROM ScholarExpenses " +
				"GROUP BY YEAR(start_date)";
		TypedQuery<Object[]> query = entityManager.createQuery(queryString, Object[].class);
		List<Object[]> rows = query.getResultList();

		Map<Integer, Integer> results = new HashMap<>();
		for (Object[] row : rows) {
			int year = ((Number) row[0]).intValue();
			int total = ((Number) row[1]).intValue();
			results.put(year, total);
		}

		return results;
	}

	  // Get the grand expenditure for all scholars
	  public int getGrandExpenditureForAllScholars() {
		    List<ScholarExpenses> allExpenses = scholarExpenseRepo.findAll();
		    int grandExpenditure = 0;
		    for (ScholarExpenses expenses : allExpenses) {
				grandExpenditure += expenses.getScholarTotalPerTerm();
		    }
		    return grandExpenditure;
	  }

	  private ScholarExpenseDto mapToDto(ScholarExpenses scholarExpenses) {
		    ScholarExpenseDto scholarExpenseDto = new ScholarExpenseDto();
		    scholarExpenseDto.setId(scholarExpenses.getId());
		    scholarExpenseDto.setTerm(scholarExpenses.getTerm());
		    scholarExpenseDto.setForm(scholarExpenses.getForm());
//        scholarExpenseDto.setSchoolName(scholarExpenses.getSchoolName());
		    scholarExpenseDto.setStart_date(scholarExpenses.getStart_date());
		    scholarExpenseDto.setShopping(scholarExpenses.getShopping());
		    scholarExpenseDto.setTransport(scholarExpenses.getTransport());
		    scholarExpenseDto.setSchoolFees(scholarExpenses.getSchoolFees());
		    scholarExpenseDto.setUpkeep(scholarExpenses.getUpkeep());
		    scholarExpenseDto.setTotal(scholarExpenses.getScholarTotalPerTerm());
		    scholarExpenseDto.setApplication(scholarExpenses.getApplication());
		    return scholarExpenseDto;
	  }

	  private ScholarExpenses mapToEntity(ScholarExpenseDto scholarExpenseDto) {
		    ScholarExpenses scholarExpenses = new ScholarExpenses();
		    scholarExpenses.setId(scholarExpenseDto.getId());
		    scholarExpenses.setForm(scholarExpenseDto.getForm());
		    scholarExpenses.setTerm(scholarExpenseDto.getTerm());
//        scholarExpenses.setSchoolName(scholarExpenseDto.getSchoolName());
		    scholarExpenses.setStart_date(scholarExpenseDto.getStart_date());
		    scholarExpenses.setShopping(scholarExpenseDto.getShopping());
		    scholarExpenses.setSchoolFees(scholarExpenseDto.getSchoolFees());
		    scholarExpenses.setTransport(scholarExpenseDto.getTransport());
		    scholarExpenses.setUpkeep(scholarExpenseDto.getUpkeep());
		    scholarExpenses.setScholarTotalPerTerm(scholarExpenseDto.getTotal());
		    scholarExpenses.setApplication(scholarExpenseDto.getApplication());
		    return scholarExpenses;
	  }

}
