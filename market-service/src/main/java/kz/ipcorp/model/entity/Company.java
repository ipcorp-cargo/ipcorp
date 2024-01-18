package kz.ipcorp.model.entity;

import jakarta.persistence.*;
import kz.ipcorp.model.enumuration.Status;
import lombok.Data;
import lombok.ToString;


import java.util.UUID;

@Entity
@Table(name = "companies")
@Data
public class Company {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true)
    private String name;


    @Column(name = "registration_number")
    private Long registrationNumber;

    @Column(name = "registration_addres")
    private String registrationAddress;

    @Column(name = "business_activities")
    private String businessActivities;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;

    @Column(name = "path_to_business_license")
    private String pathToBusinessLicense;
}
