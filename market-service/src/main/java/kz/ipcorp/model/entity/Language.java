package kz.ipcorp.model.entity;


import jakarta.persistence.*;
import lombok.*;

import javax.annotation.processing.Generated;

@Entity
@Table(name = "languages")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
