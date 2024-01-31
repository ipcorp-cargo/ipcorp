package kz.ipcorp.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "verifications")
@Data
public class Verification {
    public static final int VERIFICATION_CODE_LENGTH = 6;
    @Id
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "code", length = VERIFICATION_CODE_LENGTH)
    private String code;

    @Column(name = "is_valid")
    private boolean isValid;

    @Column(name = "count")
    private int count;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    public Verification() {

    }
    public Verification(String email, String code){
        this.email = email;
        this.code = code;
        this.isValid = false;
        this.count = 0;
    }
}
