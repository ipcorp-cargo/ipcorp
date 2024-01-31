package kz.ipcorp.model.DTO;

import kz.ipcorp.model.enumuration.StatusVerify;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanyVerifyDTO {
    private UUID companyID;
    private StatusVerify status;
}
