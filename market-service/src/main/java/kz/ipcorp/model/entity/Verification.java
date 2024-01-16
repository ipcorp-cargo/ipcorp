package kz.ipcorp.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "verifications")
@Data
@ToString(exclude = {"seller"})
public class Verification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;
    //XXX XXX
    @Column(name = "code")
    private Integer code;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed = Boolean.FALSE;
}
