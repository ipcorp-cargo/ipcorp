package kz.ipcorp.model.DTO;

import lombok.Data;

@Data
public class SignUpRequestDTO {

    private String phoneNumber;
    private String password;
    private String verificationCode;
}
