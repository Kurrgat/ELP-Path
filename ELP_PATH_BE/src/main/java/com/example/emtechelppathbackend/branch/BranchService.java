package com.example.emtechelppathbackend.branch;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BranchService {

    CustomResponse<List<Branch>> displayAllBranches();

    CustomResponse<Optional<Branch>> displayBranchById(Long id);

    Branch addNewBranch(Branch branch);

    Branch updateBranchById(Long id, Branch updatedBranch);

    boolean deleteBranchById(Long id);
}
