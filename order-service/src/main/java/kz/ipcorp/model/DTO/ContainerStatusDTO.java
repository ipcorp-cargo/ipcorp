package kz.ipcorp.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ContainerStatusDTO {
    private UUID containerId;
    private UUID statusId;
}