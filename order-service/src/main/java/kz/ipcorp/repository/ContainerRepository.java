package kz.ipcorp.repository;

import kz.ipcorp.model.entity.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContainerRepository extends JpaRepository<Container, UUID> {
    Optional<Container> findContainerByName(String containerName);
}
