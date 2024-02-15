package kz.ipcorp.model.DTO;

import kz.ipcorp.exception.NotConfirmedException;
import kz.ipcorp.model.entity.Branch;
import kz.ipcorp.model.entity.Language;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BranchViewDTO {
    private UUID id;
    private String address;

    public BranchViewDTO(Branch branch, String language){
        this.id = branch.getId();
        Language acceptLanguage = branch.getLanguage();
        switch (language) {
            case "ru" ->  this.address = acceptLanguage.getRussian();
            case "en" ->  this.address = acceptLanguage.getEnglish();
            case "kk" ->  this.address = acceptLanguage.getKazakh();
            case "cn" ->  this.address = acceptLanguage.getChinese();
            default -> throw new NotConfirmedException("accept localValue not found");
        }
    }
}