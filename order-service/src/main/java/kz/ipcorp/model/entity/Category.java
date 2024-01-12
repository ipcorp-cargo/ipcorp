package kz.ipcorp.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String categoryName;

    @Column(name = "icon_path")
    private String iconPath;
}
