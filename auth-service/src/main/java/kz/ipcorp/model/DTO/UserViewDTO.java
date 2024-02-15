package kz.ipcorp.model.DTO;


import kz.ipcorp.model.entity.Branch;
import kz.ipcorp.model.entity.Language;
import kz.ipcorp.model.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserViewDTO {
    private UUID userId;
    private String phoneNumber;
    private String branch;

    public UserViewDTO() {}

    public UserViewDTO(User user, String language) {
        this.userId = user.getId();
        this.phoneNumber = user.getPhoneNumber();
        Branch branch = user.getBranch();
        if (branch != null) {

            Language acceptLanguage = branch.getLanguage();

            if (language != null) {

                switch (language) {
                    case "ru" -> {
                        this.branch = acceptLanguage.getRussian();
                    }
                    case "kk" -> {
                        this.branch = acceptLanguage.getKazakh();
                    }
                    case "en" -> {
                        this.branch = acceptLanguage.getEnglish();
                    }
                    case "cn" -> {
                        this.branch = acceptLanguage.getChinese();
                    }
                }

            }

        }
    }
}
