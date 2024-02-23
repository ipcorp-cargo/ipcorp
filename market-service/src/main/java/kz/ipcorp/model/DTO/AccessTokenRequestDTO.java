package kz.ipcorp.model.DTO;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Repository
public class AccessTokenRequestDTO {
    private String refreshToken;
}
