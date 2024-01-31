package kz.ipcorp.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SMSVerificationDTO {
    private String phoneNumber;
    private String verificationCode;
}
