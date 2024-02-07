package kz.ipcorp.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import kz.ipcorp.feign.MediaFeignClient;
import kz.ipcorp.model.DTO.CompanyCreateDTO;
import kz.ipcorp.model.DTO.CompanyReadDTO;
import kz.ipcorp.model.DTO.CompanyVerifyDTO;
import kz.ipcorp.model.enumuration.Status;
import kz.ipcorp.model.enumuration.StatusVerify;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final MediaFeignClient mediaFeignClient;
    private final Logger log = LogManager.getLogger(CompanyController.class);


    @GetMapping()
    public ResponseEntity<List<CompanyReadDTO>> getCompanies() {
        log.info("IN getCompanies");
        return new ResponseEntity<>(companyService.getCompanies(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CompanyReadDTO>> getCompaniesByFilter(@Parameter(in = ParameterIn.QUERY, name = "status",
            description = "Status filter",
            schema = @Schema(type = "string", allowableValues = {"NOT_UPLOADED", "UPLOADED", "ACCEPT", "DENY"}))
                                                                     @RequestParam("status") Status status) {
        log.info("IN getCompaniesByFilter");
        return new ResponseEntity<>(companyService.getCompaniesByFilter(status), HttpStatus.OK);
    }

    @GetMapping("/seller")
    public ResponseEntity<CompanyReadDTO> getCompany(Principal principal) {
        log.info("IN getCompany with sellerId: {}", UUID.fromString(principal.getName()));
        return new ResponseEntity<>(companyService.getCompany(UUID.fromString(principal.getName())), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CompanyReadDTO> registerCompany(@RequestBody CompanyCreateDTO companyDTO, Principal principal) {
        log.info("IN registerCompany - companyName: {}, sellerId: {}", companyDTO.getName(), UUID.fromString(principal.getName()));
        return new ResponseEntity<>(companyService.registerCompany(companyDTO, UUID.fromString(principal.getName())), HttpStatus.CREATED);
    }

    @PostMapping(path = "/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerDocument(@RequestParam("companyId") UUID companyId, @RequestParam("businessLicense") MultipartFile businessLicense) {
        log.info("IN registerDocument - companyId: {}", companyId);
        String path = mediaFeignClient.getPathName(businessLicense);
        companyService.savePath(companyId, path);
        return new ResponseEntity<>(path, HttpStatus.OK);
    }

    @PatchMapping("/verify")
    @Parameter(in = ParameterIn.QUERY, name = "status",
            description = "set status for company",
            schema = @Schema(type = "string", allowableValues = {"ACCEPT", "DENY"}))
    public ResponseEntity<HttpStatus> verifyCompany(@RequestBody CompanyVerifyDTO companyVerifyDTO) {
        companyService.verifyCompany(companyVerifyDTO.getStatus(), companyVerifyDTO.getCompanyID());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
