package kz.ipcorp.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderUpdateStatus {
    private UUID statusId;
    private String trackCode;
}