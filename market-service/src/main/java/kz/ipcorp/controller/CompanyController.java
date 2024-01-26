package kz.ipcorp.controller;

import kz.ipcorp.feign.LicenseFeignClient;
import kz.ipcorp.model.DTO.CompanyCreateDTO;
import kz.ipcorp.model.DTO.CompanyReadDTO;
import kz.ipcorp.model.DTO.CompanyVerifyDTO;
import kz.ipcorp.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final LicenseFeignClient licenseFeignClient;
    private final Logger log = LogManager.getLogger(CompanyController.class);

    @GetMapping
    public ResponseEntity<CompanyReadDTO> getCompany(Principal principal){
        log.info("IN getCompany with sellerId: {}", UUID.fromString(principal.getName()));
        return new ResponseEntity<>(companyService.getCompany(UUID.fromString(principal.getName())), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> registerCompany(@RequestBody CompanyCreateDTO companyDTO, Principal principal){
        log.info("IN registerCompany - companyName: {}, sellerId: {}", companyDTO.getName(), UUID.fromString(principal.getName()));
        companyService.registerCompany(companyDTO, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerDocument(@RequestParam("companyName") String companyName, @RequestParam("businessLicense") MultipartFile businessLicense){
        log.info("IN registerDocument - companyName: {}", companyName);
        String path = licenseFeignClient.getPathName(businessLicense);
        companyService.savePath(companyName, path);
        return new ResponseEntity<>(path, HttpStatus.OK);
    }

    @PatchMapping("/verify")
    public ResponseEntity<HttpStatus> verifyCompany(@RequestBody CompanyVerifyDTO companyVerifyDTO, Principal principal) {
        companyService.verifyCompany(companyVerifyDTO, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
