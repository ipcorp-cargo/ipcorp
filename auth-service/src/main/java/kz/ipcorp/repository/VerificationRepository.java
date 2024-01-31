package kz.ipcorp.repository;

import kz.ipcorp.model.entity.Verification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends CrudRepository<Verification, String> {
}