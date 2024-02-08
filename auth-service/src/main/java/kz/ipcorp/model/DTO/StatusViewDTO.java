package kz.ipcorp.model.DTO;

import jakarta.persistence.Column;
import kz.ipcorp.model.entity.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusViewDTO {
    private UUID id;
    private String status;
    private String description;
}
