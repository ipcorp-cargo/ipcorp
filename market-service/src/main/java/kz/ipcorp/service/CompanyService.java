package kz.ipcorp.service;

import kz.ipcorp.config.AuthInterceptor;
import kz.ipcorp.exception.AuthenticationException;
import kz.ipcorp.exception.NotConfirmedException;
import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.feign.JwtFeignClient;
import kz.ipcorp.model.DTO.CompanyCreateDTO;
import kz.ipcorp.model.DTO.CompanyReadDTO;
import kz.ipcorp.model.entity.Company;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.model.enumuration.Status;
import kz.ipcorp.model.enumuration.StatusVerify;
import kz.ipcorp.repository.CompanyRepository;
import kz.ipcorp.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final SellerRepository sellerRepository;
    private final JwtFeignClient jwtFeignClient;
    private final AuthInterceptor authInterceptor;
    private final Logger log = LogManager.getLogger(CompanyService.class);

    @Transactional(readOnly = true)
    public CompanyReadDTO getCompany(UUID id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new NotFoundException("seller is not found"));
        Company company = seller.getCompany();
        if (company == null) {
            log.error("In getCompany - company doesn't exists9");
            throw new NotFoundException("company doesn't exists");
        }
        log.info("IN getCompany - companyName: {}", company.getName());
        return companyToCompanyDTO(company);
    }

    public CompanyReadDTO companyToCompanyDTO(Company company){
        CompanyReadDTO companyReadDTO = new CompanyReadDTO(company);
        companyReadDTO.setName(company.getName());
        companyReadDTO.setRegistrationNumber(company.getRegistrationNumber());
        companyReadDTO.setRegistrationAddress(company.getRegistrationAddress());
        companyReadDTO.setBusinessActivity(company.getBusinessActivity());
        companyReadDTO.setStatus(company.getStatus());
        companyReadDTO.setPathToBusinessLicense(company.getPathToBusinessLicense());
        return companyReadDTO;
    }

    @Transactional
    public CompanyReadDTO registerCompany(CompanyCreateDTO companyCreateDTO, UUID id){
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new NotFoundException("seller is not found"));
        if (seller.getCompany() != null) {
            log.error("IN registerCompany - company already exists");
            throw new NotFoundException("company already exists");
        }
        Company company = new Company();
        company.setName(companyCreateDTO.getName());
        company.setRegistrationAddress(companyCreateDTO.getRegistrationAddress());
        company.setBusinessActivity(companyCreateDTO.getBusinessActivity());
        company.setRegistrationNumber(companyCreateDTO.getRegistrationNumber());
        company.setCreatedAt(LocalDate.now());
        company.setSeller(seller);
        Company savedCompany = companyRepository.save(company);
        seller.setCompany(savedCompany);
        log.info("IN registerCompany - companyName: {}", company.getName());
        sellerRepository.save(seller);
        return new CompanyReadDTO(savedCompany);
    }

    @Transactional
    public void savePath(UUID companyId, String pathToBusinessLicense){
        log.info("IN savePath - companyName: {}, path: {}", companyId, pathToBusinessLicense);
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new NotFoundException("company not found"));
        if (company.getStatus() == Status.ACCEPT || company.getStatus() == Status.UPLOADED) {
            throw new NotConfirmedException("company can not uploaded document. Document status is accept or waiting");
        }else{
            company.setPathToBusinessLicense(pathToBusinessLicense);
        }
        company.setStatus(Status.UPLOADED);
        companyRepository.saveAndFlush(company);
    }

    @Transactional
    public void saveCompany(Company company) {
        log.info("IN saveCompany - companyName: {}", company.getName());
        companyRepository.save(company);
    }

    @Transactional
    public void verifyCompany(String token, StatusVerify statusVerify, UUID companyId) {
        checkToken(token);
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new NotFoundException(
                        String.format("company with %s id not found", companyId)
                )
        );
        Status status = convertStatus(statusVerify);
        company.setStatus(status);
        if (status == Status.ACCEPT) {
            company.setExpiredAt(LocalDateTime.now().plusYears(1));
        }
        companyRepository.saveAndFlush(company);
    }

    private Status convertStatus(StatusVerify statusVerify) {
        return switch (statusVerify) {
            case ACCEPT -> Status.ACCEPT;
            case DENY -> Status.DENY;
        };
    }

    @Transactional(readOnly = true)
    public List<CompanyReadDTO> getCompanies(String token, PageRequest of) {
        checkToken(token);
        List<CompanyReadDTO> companies = new ArrayList<>();
        for (Company company : companyRepository.findAll(of)) {
            companies.add(new CompanyReadDTO(company));
        }
        return companies;
    }


    @Transactional(readOnly = true)
    public List<CompanyReadDTO> getCompaniesByFilter(String token, Status status) {
        log.info("IN getCompaniesByFilter - CompanyService");
        checkToken(token);
        List<CompanyReadDTO> companies = new ArrayList<>();
        for (Company company : companyRepository.findAllByStatus(status)) {
            companies.add(new CompanyReadDTO(company));
        }
        return companies;
    }

    @Transactional(readOnly = true)
    public List<CompanyReadDTO> getCompaniesByName(String token, String companyName, PageRequest of) {
        checkToken(token);
        return companyRepository.findByNameContainingIgnoreCase(companyName, of)
                .stream()
                .map(CompanyReadDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompanyReadDTO> getCompaniesByDate(String token, LocalDate date, PageRequest of) {
        checkToken(token);
        return companyRepository.findByCreatedAt(date, of)
                .stream()
                .map(CompanyReadDTO::new)
                .toList();
    }
    private void checkToken(String token){
        log.info("token: {}", token);
        authInterceptor.setToken(token);
        try {
            String status = jwtFeignClient.testMethod("Bearer " + token);
            if(!status.equals("status")){
                log.info("status: " + status);
            }
        } catch (Exception e){
            throw new AuthenticationException("this admin not authorized");
        }
    }
}





