package com.example.emtechelppathbackend.branch;

import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/branch")
@RequiredArgsConstructor
public class BranchController {

	private final BranchService branchService;

	@GetMapping("/all")
	public ResponseEntity<CustomResponse<List<Branch>>> displayAllBranches() {
		var response = branchService.displayAllBranches();
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}

	@GetMapping("/display/{id}")
	public ResponseEntity<CustomResponse<Optional<Branch>>> displayBranchById(@PathVariable Long id) {
		var response = branchService.displayBranchById(id);
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}

	@PostMapping("/add-new-branch")
	public ResponseEntity<Branch> addNewBranch(@RequestBody Branch branch) {
		Branch addedBranch = branchService.addNewBranch(branch);
		return new ResponseEntity<>(addedBranch, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Branch> updateBranchById(@PathVariable Long id, @RequestBody Branch updatedBranch) {
		Branch updated = branchService.updateBranchById(id, updatedBranch);
		if (updated != null) {
			return new ResponseEntity<>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<Void> deleteBranchById(@PathVariable Long id) {
		boolean deleted = branchService.deleteBranchById(id);
		if (deleted) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
