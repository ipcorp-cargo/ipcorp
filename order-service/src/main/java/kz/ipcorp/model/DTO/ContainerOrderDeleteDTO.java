package kz.ipcorp.model.DTO;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ContainerOrderDeleteDTO {
    private UUID containerId;
    private UUID orderId;
}
