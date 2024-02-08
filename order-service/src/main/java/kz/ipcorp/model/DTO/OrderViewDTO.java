package kz.ipcorp.model.DTO;

import kz.ipcorp.model.entity.Order;
import kz.ipcorp.model.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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
