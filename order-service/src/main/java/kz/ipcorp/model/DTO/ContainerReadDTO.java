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
public class ContainerReadDTO {
    private UUID id;
    private String name;
    private List<OrderViewDTO> orders = new ArrayList<>();

    public ContainerReadDTO(Container container) {
        this.id = container.getId();
        this.name = container.getName();
        if (container.getOrders() != null) {
            for (Order order : container.getOrders()) {
                orders.add(new OrderViewDTO(order));
            }
        }
    }
}
