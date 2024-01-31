package kz.ipcorp.model.DTO;

import kz.ipcorp.model.entity.Company;
import kz.ipcorp.model.enumuration.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class CompanyReadDTO {
    private UUID id;
    private String name;
    private Long registrationNumber;
    private String registrationAddress;
    private String businessActivity;
    private Status status;
    private String pathToBusinessLicense;

    public CompanyReadDTO() {
    }

    public CompanyReadDTO(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.registrationNumber = company.getRegistrationNumber();
        this.registrationAddress = company.getRegistrationAddress();
        this.businessActivity = company.getBusinessActivity();
        this.status = company.getStatus();
        this.pathToBusinessLicense = company.getPathToBusinessLicense();
    }
}
