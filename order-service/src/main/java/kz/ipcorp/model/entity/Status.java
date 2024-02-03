package kz.ipcorp.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
/**
 * Status list:
     * ORDER_RECEIVED
     * SHIPPED_FROM_WAREHOUSE
     * UNDER_CUSTOMS_CLEARANCE
     * ALMATY_SORTING_POINT
     * READY_TO_PICKUP
     * DELIVERED
 *
 * */
@Entity
@Table(name = "statuses")
@Data
public class Status {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true)
    private String name;
}