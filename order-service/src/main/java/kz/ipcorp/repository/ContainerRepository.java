package kz.ipcorp.repository;

import kz.ipcorp.model.entity.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContainerRepository extends JpaRepository<Container, UUID> {

    Optional<Container> findContainerByName(String containerName);

    @Modifying
    @Query(value = "DELETE FROM container_orders WHERE container_id = :containerId AND order_id = :orderId", nativeQuery = true)
    void deleteOrderFromContainer(UUID containerId, UUID orderId);
}
