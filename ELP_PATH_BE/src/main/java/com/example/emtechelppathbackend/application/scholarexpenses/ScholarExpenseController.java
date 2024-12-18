package com.example.emtechelppathbackend.application.scholarexpenses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scholar/expenses")
public class ScholarExpenseController {
	  private final ScholarExpenseService scholarExpenseService;

	  public ScholarExpenseController(ScholarExpenseService scholarExpenseService) {
		    this.scholarExpenseService = scholarExpenseService;
	  }

	  @PostMapping("/{scholarId}/add")
	  public ResponseEntity<ScholarExpenseDto> addScholarExpense(@PathVariable(value = "scholarId") Long scholarId, @RequestBody ScholarExpenseDto scholarExpenseDto) {
		    return new ResponseEntity<>(scholarExpenseService.addScholarExpenses(scholarId, scholarExpenseDto), HttpStatus.CREATED);
	  }

	  @GetMapping("/{scholarId}/view")
	  public List<ScholarExpenseDto> viewScholarExpenses(@PathVariable(value = "scholarId") Long scholarId) {
		    return scholarExpenseService.findExpensesByApplicationId(scholarId);
	  }

	  //Totals per student
	  @GetMapping("/student-total-expense/{applicationId}")
	  public int getScholarTotalExpenses(@PathVariable Long applicationId) {
		    return scholarExpenseService.getTotalExpenditurePerScholar(applicationId);
	  }

	  //grand Totals i.e. for all students
	  @GetMapping("/grand-totals")
	  public int getGrossTotalExpenses() {
		    return scholarExpenseService.getGrandExpenditureForAllScholars();
	  }


	  @PutMapping("/update/{scholarExpensesId}")
	  public ResponseEntity<ScholarExpenseDto> updateScholarExpenses(
		    @PathVariable Long scholarExpensesId,
		    @RequestBody ScholarExpenseDto updatedScholarExpenseDto) {
		    ScholarExpenseDto updatedScholarExpenses = scholarExpenseService.updateScholarExpenses(scholarExpensesId, updatedScholarExpenseDto);
		    return ResponseEntity.ok(updatedScholarExpenses);
	  }

	  @DeleteMapping("/delete/{scholarExpenseId}")
	  public ResponseEntity<String> deleteScholarExpense(@PathVariable Long scholarExpenseId) {
		    scholarExpenseService.deleteScholarExpenseById(scholarExpenseId);
		    return ResponseEntity.ok("Scholar expense deleted successfully");
	  }

	  @GetMapping("/total_expense_per_year")
	  public Map<Integer, Integer> getTotalExpenseByYear() {
		    return scholarExpenseService.getTotalExpenseByYear();
	  }

}
