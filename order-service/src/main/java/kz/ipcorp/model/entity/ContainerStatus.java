package kz.ipcorp.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "container_status")
@Data
public class ContainerStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "container_id")
    private Container container;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
