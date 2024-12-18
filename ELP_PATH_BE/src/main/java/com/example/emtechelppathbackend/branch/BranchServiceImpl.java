package com.example.emtechelppathbackend.branch;

import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

	private final BranchRepository branchRepository;

	@Override
	public CustomResponse<List<Branch>> displayAllBranches() {
		CustomResponse<List<Branch>> response = new CustomResponse<>();
		try {
			var total = branchRepository.findAll();
			if (total == null) {
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setMessage("No event found");
				response.setPayload(null);
			} else {
				response.setPayload(total);
				response.setMessage("Found");
				response.setStatusCode(HttpStatus.OK.value());
			}
		}catch(
				Exception e)

		{
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

		}
		return response;
	}

	@Override
	public CustomResponse<Optional<Branch>> displayBranchById(Long id) {
		CustomResponse<Optional<Branch>> response=new CustomResponse<>();
		try {
			var total= branchRepository.findById(id);
			if(total==null){
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setMessage("No branch found");
				response.setPayload(null);
			}else {
				response.setPayload(total);
				response.setMessage("Found");
				response.setStatusCode(HttpStatus.OK.value());
			}
		}catch (Exception e){
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

		}
		return response;
	}

	@Override
	public Branch addNewBranch(Branch branch) {
		return branchRepository.save(branch);
	}

	@Override
	public Branch updateBranchById(Long id, Branch updatedBranch) {
		Optional<Branch> existingBranch = branchRepository.findById(id);
		if (existingBranch.isPresent()) {
			Branch branch = existingBranch.get();
			branch.setBranchName(updatedBranch.getBranchName());
			branch.setAdditionalInformation(updatedBranch.getAdditionalInformation());
			return branchRepository.save(branch);
		}
		return null; // todo : exception
	}

	@Override
	public boolean deleteBranchById(Long id) {
		if (branchRepository.existsById(id)) {
			branchRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
