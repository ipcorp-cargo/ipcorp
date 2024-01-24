package kz.ipcorp.model.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateDTO {
    private String trackCode;
    private String orderName;
}
