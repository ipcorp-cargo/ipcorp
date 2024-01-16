package kz.ipcorp.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenRequestDTO {
    private String refreshToken;
}
