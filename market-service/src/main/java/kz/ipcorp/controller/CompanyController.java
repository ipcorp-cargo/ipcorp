package kz.ipcorp.controller;

import kz.ipcorp.feign.LicenseFeignClient;
import kz.ipcorp.model.DTO.CompanyDTO;
import kz.ipcorp.model.DTO.CompanyReadDTO;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.service.CompanyService;
import kz.ipcorp.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {
    private final CompanyService companyService;
    private final LicenseFeignClient licenseFeignClient;

    @GetMapping
    public ResponseEntity<List<CompanyReadDTO>> getAll(){
        return new ResponseEntity<>(companyService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/findByCompanyName")
    public ResponseEntity<CompanyReadDTO> findCompanyByName(@RequestParam("companyName") String companyName){
        return new ResponseEntity<>(companyService.getByCompanyName(companyName), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody CompanyDTO companyDTO, Principal principal){
        System.out.println("==================================");
        System.out.println(principal.getName());
        System.out.println("==================================");
        companyService.addCompany(companyDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "/register_document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerDocument(@RequestParam("companyName") String companyName, @RequestParam("businessLicense") MultipartFile businessLicense){
        String path = licenseFeignClient.getPathName(businessLicense);
        companyService.savePath(companyName, path);
        return new ResponseEntity<>(path, HttpStatus.OK);
    }
}
