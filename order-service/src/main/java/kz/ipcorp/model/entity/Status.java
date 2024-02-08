package kz.ipcorp.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
@Entity
@Table(name = "statuses")
@Data
public class Status {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;
}