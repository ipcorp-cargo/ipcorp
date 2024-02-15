package kz.ipcorp.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderCreateDTO {
    private String trackCode;
    private String orderName;
}
