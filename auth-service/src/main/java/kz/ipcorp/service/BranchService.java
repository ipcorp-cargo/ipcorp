package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.BranchViewDTO;
import kz.ipcorp.model.entity.Branch;
import kz.ipcorp.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchService {
    private final BranchRepository branchRepository;
    private final UserService userService;
    private final Logger log = LogManager.getLogger(BranchService.class);
    public List<BranchViewDTO> getAllBranches(String language){
        log.info("IN getAllBranches - in BranchService");
        List<BranchViewDTO> branchViewDTOS = new ArrayList<>();
        for(Branch branch : branchRepository.findAll()){
            branchViewDTOS.add(new BranchViewDTO(branch, language));
        }
        return branchViewDTOS;
    }

    @Transactional
    public void updateBranch(UUID branchId, UUID userId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException(String.format("branch with branchId %s not found", branchId)));
        userService.addBranch(branch, userId);
    }
}