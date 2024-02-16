package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.BranchViewDTO;
import kz.ipcorp.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<List<BranchViewDTO>> getAllBranches(
            @CookieValue(name = "Accept-Language", defaultValue = "ru") String language){
        return new ResponseEntity<>(branchService.getAllBranches(language), HttpStatus.OK);
    }


}