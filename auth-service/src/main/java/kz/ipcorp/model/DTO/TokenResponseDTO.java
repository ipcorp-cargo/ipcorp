package kz.ipcorp.model.DTO;

import lombok.Data;

@Data
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;

}
