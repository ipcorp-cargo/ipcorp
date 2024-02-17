package kz.ipcorp.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import kz.ipcorp.feign.MediaFeignClient;
import kz.ipcorp.model.DTO.CompanyCreateDTO;
import kz.ipcorp.model.DTO.CompanyReadDTO;
import kz.ipcorp.model.DTO.CompanyVerifyDTO;
import kz.ipcorp.model.enumuration.Status;
import kz.ipcorp.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final MediaFeignClient mediaFeignClient;
    private final Logger log = LogManager.getLogger(CompanyController.class);


    @GetMapping("/company")
    public ResponseEntity<List<CompanyReadDTO>> getCompanies(@RequestParam(name = "Authorization") String token,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("IN getCompanies");
        return new ResponseEntity<>(companyService.getCompanies(token, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CompanyReadDTO>> getCompaniesByFilter(@RequestParam(name = "Authorization") String token,
                                                                     @Parameter(in = ParameterIn.QUERY, name = "status",
            description = "Status filter",
            schema = @Schema(type = "string", allowableValues = {"NOT_UPLOADED", "UPLOADED", "ACCEPT", "DENY"}))
                                                                     @RequestParam("status") Status status) {
        log.info("IN getCompaniesByFilter");
        return new ResponseEntity<>(companyService.getCompaniesByFilter(token,status), HttpStatus.OK);
    }

    @GetMapping("/filter/date")
    public ResponseEntity<List<CompanyReadDTO>> getCompaniesByDate(@RequestParam(name = "Authorization") String token,
                                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("IN getCompaniesByDate");
        return new ResponseEntity<>(companyService.getCompaniesByDate(token, date, PageRequest.of(page, size)), HttpStatus.OK);
    }
    @GetMapping("/filter/name")
    public ResponseEntity<List<CompanyReadDTO>> getCompaniesByName(@RequestParam(name = "Authorization") String token,
                                                                   @RequestParam("companyName") String companyName,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("IN getCompaniesByName");
        return new ResponseEntity<>(companyService.getCompaniesByName(token,companyName, PageRequest.of(page, size)), HttpStatus.OK);
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
    public ResponseEntity<HttpStatus> verifyCompany(@RequestParam(name = "Authorization") String token,
                                                    @RequestBody CompanyVerifyDTO companyVerifyDTO) {
        companyService.verifyCompany(token, companyVerifyDTO.getStatus(), companyVerifyDTO.getCompanyID());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
