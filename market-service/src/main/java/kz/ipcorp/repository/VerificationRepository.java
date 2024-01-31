package kz.ipcorp.repository;


import kz.ipcorp.model.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VerificationRepository extends JpaRepository<Verification, String> {

}
