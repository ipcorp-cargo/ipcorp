package kz.ipcorp.model.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CompanyDTO {
    private String name;
    private String registrationAddress;
    private List<String> businessActivities;
}
