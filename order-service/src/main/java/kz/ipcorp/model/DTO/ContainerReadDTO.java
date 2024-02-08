package kz.ipcorp.model.DTO;

import kz.ipcorp.model.entity.Container;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ContainerReadDTO {
    private UUID id;
    private String name;

    public ContainerReadDTO(Container container) {
        this.id = container.getId();
        this.name = container.getName();

    }
}
