package kz.ipcorp.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "track_code")
    private String trackCode;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "container_id", referencedColumnName = "id")
    private Container container;

    @OneToMany
    private List<Status> statuses;
}
