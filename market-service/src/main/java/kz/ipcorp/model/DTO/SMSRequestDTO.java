package kz.ipcorp.model.DTO;

import kz.ipcorp.model.enumuration.SMSRequestType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SMSRequestDTO {
    private String email;
    private SMSRequestType smsRequestType;
}
