package kz.ipcorp.model.entity;

import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "name")
    private String name;

    @OneToMany
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<Order> orders;
}
