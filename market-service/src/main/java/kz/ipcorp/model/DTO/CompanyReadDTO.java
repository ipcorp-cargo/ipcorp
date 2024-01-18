package kz.ipcorp.model.DTO;

import kz.ipcorp.model.enumuration.Status;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompanyReadDTO {
    private String name;
    private Long registrationNumber;
    private String registrationAddress;
    private List<String> businessActivities;
    private Status status;
    private String pathToBusinessLicense;
}
