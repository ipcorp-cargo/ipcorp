package kz.ipcorp.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.ipcorp.model.enumuration.SMSRequestType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SMSRequestDTO {
    private String email;

    private SMSRequestType smsRequestType;
}
