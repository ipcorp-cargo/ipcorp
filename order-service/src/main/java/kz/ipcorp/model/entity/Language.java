package kz.ipcorp.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "languages")
@Data
public class Language {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kazakh")
    private String kazakh;

    @Column(name = "english")
    private String english;

    @Column(name = "russian")
    private String russian;

    @Column(name = "chinese")
    private String chinese;
}
