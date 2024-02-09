package kz.ipcorp.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "statuses")
public class Status {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @OneToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

}

