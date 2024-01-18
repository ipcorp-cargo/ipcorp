package kz.ipcorp.repository;

import kz.ipcorp.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findCompanyByName(String companyName);

    @Modifying
    @Query("UPDATE Company c SET c.pathToBusinessLicense = :pathToBusinessLicense WHERE c.name = :companyName")
    void savePathToBusinessLicense(String companyName, String pathToBusinessLicense);
}