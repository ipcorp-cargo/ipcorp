package kz.ipcorp.repository;

import kz.ipcorp.model.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, UUID> {
    List<OrderStatus> findByOrder_Id(UUID orderId);
}
