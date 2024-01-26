package kz.ipcorp.model.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    /**
     *
     *
     *
     * */
}
