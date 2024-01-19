package kz.ipcorp.service;

import kz.ipcorp.model.DTO.CompanyDTO;
import kz.ipcorp.model.DTO.CompanyReadDTO;
import kz.ipcorp.model.entity.Company;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.model.enumuration.Status;
import kz.ipcorp.repository.CompanyRepository;
import kz.ipcorp.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final SellerRepository sellerRepository;


    @Transactional(readOnly = true)
    public CompanyReadDTO getByCompanyName(String companyName) {
        Company company = companyRepository.findCompanyByName(companyName).orElse(null);
        CompanyReadDTO companyReadDTO = new CompanyReadDTO();
        if(company != null) {
            companyReadDTO.setName(company.getName());
            companyReadDTO.setRegistrationNumber(company.getRegistrationNumber());
            companyReadDTO.setRegistrationAddress(company.getRegistrationAddress());
            List<String> business = new ArrayList<>();
            Collections.addAll(business, company.getBusinessActivities().split(","));
            companyReadDTO.setBusinessActivities(business);
            companyReadDTO.setStatus(company.getStatus());
            companyReadDTO.setPathToBusinessLicense(company.getPathToBusinessLicense());
        }
        return companyReadDTO;
    }

    @Transactional
    public void addCompany(CompanyDTO companyDTO, String email){
        log.info("companyDTO: " + companyDTO);
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("seller not found"));
        Company company = new Company();
        company.setName(companyDTO.getName());
        company.setRegistrationAddress(companyDTO.getRegistrationAddress());
        StringBuilder businessActivities = new StringBuilder();
        List<String> businessAc = companyDTO.getBusinessActivities();
        for (int i = 0; i < businessAc.size(); i++) {
            if (i < businessAc.size() - 1) {
                businessActivities.append(businessAc.get(i)).append(",");
            }else{
                businessActivities.append(businessAc.get(i));
            }
        }
        company.setBusinessActivities(businessActivities.toString());
        company.setStatus(Status.Not_uploaded);
        company.setSeller(seller);
//        company.setSeller(sellerRepository.findByEmail(username).orElse(null));
        companyRepository.save(company);
    }

    @Transactional
    public void savePath(String companyName, String pathToBusinessLicense){
        companyRepository.savePathToBusinessLicense(companyName, pathToBusinessLicense);
    }


    @Transactional(readOnly = true)
    public List<CompanyReadDTO> getAll(){
        List<CompanyReadDTO> companies = new ArrayList<>();
        log.info("companies: " + companyRepository.findAll());
        for(Company company : companyRepository.findAll()){
            List<String> business = new ArrayList<>();
            Collections.addAll(business, company.getBusinessActivities().split(","));
           companies.add(new CompanyReadDTO(company.getName(),
                    company.getRegistrationNumber(),
                    company.getRegistrationAddress(),
                    business,
                    company.getStatus(),
                    company.getPathToBusinessLicense()));
        }
        return companies;
    }
}





