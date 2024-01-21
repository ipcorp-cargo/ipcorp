package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.CompanyCreateDTO;
import kz.ipcorp.model.DTO.CompanyReadDTO;
import kz.ipcorp.model.entity.Company;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.repository.CompanyRepository;
import kz.ipcorp.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final SellerRepository sellerRepository;


    @Transactional(readOnly = true)
    public CompanyReadDTO getCompany(UUID id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new NotFoundException("seller is not found"));
        Company company = seller.getCompany();
        if (company == null) {
            throw new NotFoundException("company doesn't exists");
        }
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
        sellerRepository.save(seller);
    }

    @Transactional
    public void savePath(String companyName, String pathToBusinessLicense){
        companyRepository.savePathToBusinessLicense(companyName, pathToBusinessLicense);
    }


//    @Transactional(readOnly = true)
//    public List<CompanyReadDTO> getAll(){
//        List<CompanyReadDTO> companies = new ArrayList<>();
//        log.info("companies: " + companyRepository.findAll());
//        for(Company company : companyRepository.findAll()){
//            List<String> business = new ArrayList<>();
//            Collections.addAll(business, company.getBusinessActivities().split(","));
//           companies.add(new CompanyReadDTO(company.getName(),
//                    company.getRegistrationNumber(),
//                    company.getRegistrationAddress(),
//                    business,
//                    company.getStatus(),
//                    company.getPathToBusinessLicense()));
//        }
//        return companies;
//    }

    @Transactional
    public void saveCompany(Company company) {
        companyRepository.save(company);
    }
}





