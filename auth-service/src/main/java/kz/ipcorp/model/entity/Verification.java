package kz.ipcorp.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "verification")
public class Verification {
    public static final int VERIFICATION_CODE_LENGTH = 6;
    @Id
    @Column(name = "phone_number", unique = true, length = 15)
    private String phoneNumber;

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
    public Verification(String phoneNumber, String code){
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.isValid = false;
        this.count = 0;
    }
}
