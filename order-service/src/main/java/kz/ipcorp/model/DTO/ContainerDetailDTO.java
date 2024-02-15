package kz.ipcorp.model.DTO;

import kz.ipcorp.exception.NotConfirmedException;
import kz.ipcorp.model.entity.Container;
import kz.ipcorp.model.entity.Language;
import kz.ipcorp.model.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class ContainerDetailDTO {
    private UUID id;
    private String name;
    private LocalDateTime createdAt;
    private List<OrderViewDTO> orders = new ArrayList<>();
    private String status;

    public ContainerDetailDTO() {}

    public ContainerDetailDTO(Container container, String language) {
        this.id = container.getId();
        this.name = container.getName();
        this.createdAt = container.getCreatedAt();
        Language acceptLanguage = container.getStatus().getStatus().getLanguage();
        switch (language) {
            case "ru" -> { this.status = acceptLanguage.getRussian();}
            case "en" -> { this.status = acceptLanguage.getEnglish(); }
            case "kk" -> { this.status = acceptLanguage.getKazakh(); }
            case "cn" -> { this.status = acceptLanguage.getChinese(); }
            default -> throw new NotConfirmedException("accept language not found");
        }
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
