package kz.ipcorp.model.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompanyCreateDTO {
    private String name;
    private String registrationAddress;
    private Long registrationNumber;
    private String businessActivity;
}
