package kz.ipcorp.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ContainerOrderDTO {
    private UUID containerId;
    private List<String> orderTrackCodes;
}