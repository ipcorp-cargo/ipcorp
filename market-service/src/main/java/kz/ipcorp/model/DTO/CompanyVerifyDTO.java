package kz.ipcorp.model.DTO;

import com.fasterxml.jackson.annotation.JsonFilter;
import kz.ipcorp.model.enumuration.Status;
import kz.ipcorp.model.enumuration.StatusVerify;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyVerifyDTO {
    private StatusVerify status;
}
