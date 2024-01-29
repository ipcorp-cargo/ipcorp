package kz.ipcorp.model.DTO;

import jakarta.persistence.Column;
import kz.ipcorp.model.entity.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StatusViewDTO {
    private UUID id;
    private String name;

    public StatusViewDTO() {}

    public StatusViewDTO(Status status) {
        this.id = status.getId();
        this.name = status.getName();
    }
}
