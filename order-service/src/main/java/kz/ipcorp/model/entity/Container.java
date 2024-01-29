package kz.ipcorp.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "containers")
@Entity
public class Container {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "container_orders",
            joinColumns = @JoinColumn(name = "container_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order> orders = new ArrayList<>();
}
