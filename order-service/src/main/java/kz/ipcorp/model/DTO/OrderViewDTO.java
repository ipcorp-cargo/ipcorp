package kz.ipcorp.model.DTO;

import jakarta.persistence.Column;
import kz.ipcorp.model.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderViewDTO {
    private UUID id;
    private String trackCode;
    private String orderName;

    public OrderViewDTO() {}
    public OrderViewDTO(Order order) {
        this.id = order.getId();
        this.trackCode = order.getTrackCode();
        this.orderName = order.getOrderName();
    }
}
