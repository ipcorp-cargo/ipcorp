package kz.ipcorp.repository;

import kz.ipcorp.model.entity.Order;
import kz.ipcorp.model.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findAllByUserId(UUID userId, Pageable pageable);
    Optional<Order> findByTrackCode(String trackCode);

    boolean existsByTrackCode(String trackCode);

    Page<Order> findAllByStatus(Pageable pageable, Status status);
}
