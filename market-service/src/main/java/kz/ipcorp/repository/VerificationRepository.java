package kz.ipcorp.repository;


import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.model.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Optional<Verification> findBySeller(Seller seller);


    @Modifying
    @Query("UPDATE Verification v SET v.isConfirmed = true WHERE v.seller = :seller")
    void confirmVerificationForSeller(Seller seller);
}
