package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.CompanyCreateDTO;
import kz.ipcorp.model.DTO.CompanyReadDTO;
import kz.ipcorp.model.DTO.CompanyVerifyDTO;
import kz.ipcorp.model.entity.Company;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.model.enumuration.Status;
import kz.ipcorp.model.enumuration.StatusVerify;
import kz.ipcorp.repository.CompanyRepository;
import kz.ipcorp.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final SellerRepository sellerRepository;
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
        CompanyReadDTO companyReadDTO = new CompanyReadDTO();
        companyReadDTO.setName(company.getName());
        companyReadDTO.setRegistrationNumber(company.getRegistrationNumber());
        companyReadDTO.setRegistrationAddress(company.getRegistrationAddress());
        companyReadDTO.setBusinessActivity(company.getBusinessActivity());
        companyReadDTO.setStatus(company.getStatus());
        companyReadDTO.setPathToBusinessLicense(company.getPathToBusinessLicense());
        return companyReadDTO;
    }

    @Transactional
    public void registerCompany(CompanyCreateDTO companyCreateDTO, UUID id){
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new NotFoundException("seller is not found"));
        if (seller.getCompany() != null) {
            log.error("IN registerCompany - company already exists");
            throw new NotFoundException("company already exists");
        }
        Company company = new Company();
        company.setName(companyCreateDTO.getName());
        company.setRegistrationAddress(companyCreateDTO.getRegistrationAddress());
        company.setBusinessActivity(companyCreateDTO.getBusinessActivity());
        company.setRegistrationNumber(company.getRegistrationNumber());
        company.setSeller(seller);
        Company savedCompany = companyRepository.save(company);
        seller.setCompany(savedCompany);
        log.info("IN registerCompany - companyName: {}", company.getName());
        sellerRepository.save(seller);
    }

    @Transactional
    public void savePath(String companyName, String pathToBusinessLicense){
        log.info("IN savePath - companyName: {}, path: {}", companyName, pathToBusinessLicense);
        companyRepository.savePathToBusinessLicense(companyName, pathToBusinessLicense);
    }

    @Transactional
    public void saveCompany(Company company) {
        log.info("IN saveCompany - companyName: {}", company.getName());
        companyRepository.save(company);
    }

    @Transactional
    public void verifyCompany(CompanyVerifyDTO companyVerifyDTO, UUID sellerID) {
        Seller seller = sellerRepository.findById(sellerID).orElseThrow(
                () -> new NotFoundException(String.format("seller with %s id not found", sellerID))
        );
        Company company = seller.getCompany();

        if (company == null) {
            throw new NotFoundException("company is not registered");
        }

        StatusVerify statusVerify = companyVerifyDTO.getStatus();
        Status status = convertStatus(statusVerify);
        company.setStatus(status);
        companyRepository.saveAndFlush(company);
    }

    private Status convertStatus(StatusVerify statusVerify) {
        return switch (statusVerify) {
            case ACCEPT -> Status.ACCEPT;
            case DENY -> Status.DENY;
        };
    }
}





