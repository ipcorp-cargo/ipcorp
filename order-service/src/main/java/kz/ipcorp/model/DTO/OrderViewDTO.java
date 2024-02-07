package kz.ipcorp.model.DTO;

import kz.ipcorp.model.entity.Order;
import kz.ipcorp.model.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderViewDTO {
    private UUID id;
    private String trackCode;
    private String orderName;
    private List<String> statusList;

    public OrderViewDTO() {}
    public OrderViewDTO(Order order) {
        this.id = order.getId();
        this.trackCode = order.getTrackCode();
        this.orderName = order.getOrderName();
        List<OrderStatus> orderStatuses = order.getOrderStatuses();
        if (orderStatuses != null) {
            for (OrderStatus orderStatus : orderStatuses) {
                statusList.add(orderStatus.getStatus().getName());
            }
        }
//        if(order.getStatus() != null) {
//            this.status = order.getStatus().getName();
//        }else {
//            this.status = "NULL";
//        }
    }
}
