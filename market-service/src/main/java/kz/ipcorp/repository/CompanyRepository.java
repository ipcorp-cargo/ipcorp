package kz.ipcorp.repository;

import kz.ipcorp.model.entity.Company;
import kz.ipcorp.model.enumuration.Status;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    List<Company> findByNameContainingIgnoreCase(String companyName, PageRequest of);
    List<Company> findByCreatedAt(LocalDate date, PageRequest of);

    List<Company> findAllByStatus(Status status, PageRequest of);
    @Modifying
    @Query("UPDATE Company c SET c.pathToBusinessLicense = :pathToBusinessLicense, c.status = :status WHERE c.id = :companyId")
    void savePathToBusinessLicense(UUID companyId, String pathToBusinessLicense, Status status);
}