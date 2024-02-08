package kz.ipcorp.model.DTO;

import kz.ipcorp.model.entity.Container;
import kz.ipcorp.model.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class ContainerDetailDTO {
    private UUID id;
    private String name;
    private List<OrderViewDTO> orders = new ArrayList<>();

    public ContainerDetailDTO() {}

    public ContainerDetailDTO(Container container) {
        this.id = container.getId();
        this.name = container.getName();
        for (Order order : container.getOrders()) {
            orders.add(
                    OrderViewDTO.builder().
                            id(order.getId())
                            .orderName(order.getOrderName())
                            .trackCode(order.getTrackCode())
                            .build()
            );
        }
    }
}
