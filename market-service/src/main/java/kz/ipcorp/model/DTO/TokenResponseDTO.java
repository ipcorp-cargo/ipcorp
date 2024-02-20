package kz.ipcorp.model.DTO;

import lombok.Getter;
import lombok.Setter;

//docker intructions
//mvn clean package (jar file create) path root - // 1
//docker down market-service
//docker image rm -f market-service
//up mar
@Getter
@Setter
public class TokenResponseDTO {
    private String accessToken;
}
