package kz.ipcorp.controller;

import kz.ipcorp.feign.LicenseFeignClient;
import kz.ipcorp.model.DTO.CompanyCreateDTO;
import kz.ipcorp.model.DTO.CompanyReadDTO;
import kz.ipcorp.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {
    private final CompanyService companyService;
    private final LicenseFeignClient licenseFeignClient;


    @GetMapping
    public ResponseEntity<CompanyReadDTO> getCompany(Principal principal){
        return new ResponseEntity<>(companyService.getCompany(UUID.fromString(principal.getName())), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> registerCompany(@RequestBody CompanyCreateDTO companyDTO, Principal principal){
        companyService.registerCompany(companyDTO, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerDocument(@RequestParam("companyName") String companyName, @RequestParam("businessLicense") MultipartFile businessLicense){
        String path = licenseFeignClient.getPathName(businessLicense);
        companyService.savePath(companyName, path);
        return new ResponseEntity<>(path, HttpStatus.OK);
    }
}
