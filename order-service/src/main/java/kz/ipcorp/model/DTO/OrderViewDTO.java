package kz.ipcorp.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderViewDTO {
    private UUID id;
    private String trackCode;
    private String orderName;

    public OrderViewDTO() {}

}
