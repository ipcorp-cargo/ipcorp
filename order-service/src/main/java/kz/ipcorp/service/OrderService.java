package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.OrderCreateDTO;
import kz.ipcorp.model.DTO.OrderDetailDTO;
import kz.ipcorp.model.DTO.OrderViewDTO;
import kz.ipcorp.model.entity.*;
import kz.ipcorp.repository.OrderRepository;
import kz.ipcorp.repository.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final Logger log = LogManager.getLogger(OrderService.class);

    @Transactional
    public OrderViewDTO createOrder(OrderCreateDTO orderCreateDTO, String userId) {
        log.info("IN createOrder - orderName: {}, userId: {}", orderCreateDTO.getOrderName(), userId);
        Order order = new Order();
        order.setTrackCode(orderCreateDTO.getTrackCode());
        order.setOrderName(orderCreateDTO.getOrderName());
        order.setUserId(UUID.fromString(userId));
        orderRepository.save(order);
        OrderViewDTO orderViewDTO = OrderViewDTO.builder()
                .id(order.getId())
                .orderName(order.getOrderName())
                .trackCode(order.getTrackCode())
                .build();
        return orderViewDTO;
    }

    @Transactional(readOnly = true)
    public Optional<Order> getByTrackCode(String trackCode) {
        log.info("IN getByTrackCode - trackCode: {}", trackCode);
        return orderRepository.findByTrackCode(trackCode);
    }

    @Transactional(readOnly = true)
    public List<OrderViewDTO> getOrders(String userId) {
        log.info("IN getOrders - userId: {}", userId);
        List<Order> ordersList = orderRepository.findAllByUserId(UUID.fromString(userId));
        List<OrderViewDTO> orders = new ArrayList<>();
        for (Order order : ordersList) {
            orders.add(
                    OrderViewDTO.builder()
                            .id(order.getId())
                            .orderName(order.getOrderName())
                            .trackCode(order.getTrackCode())
                            .build()
            );
        }
        return orders;
    }

    @Transactional
    public void addContainer(Order order, Container container) {
        log.info("IN addContainer - orderName: {}, containerName: {}", order.getOrderName(), container.getName());
        order.setContainer(container);
        orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(Order order, Status status) {
        log.info("IN addStatus - orderId: {}, statusId: {}", order.getId(), status.getId());
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus(status);
        orderStatus.setOrder(order);
        order.setStatus(status);
        orderRepository.save(order);
        orderStatusRepository.save(orderStatus);
    }

    @Transactional(readOnly = true)
    public OrderDetailDTO getOrder(String trackCode, String language) {
        Order order = orderRepository.findByTrackCode(trackCode).orElseThrow(() -> new NotFoundException("order not found"));
        log.info("IN getOrder {} {}", trackCode, language);
        OrderDetailDTO orderViewDTO = OrderDetailDTO.builder()
                .id(order.getId())
                .orderName(order.getOrderName())
                .trackCode(order.getTrackCode())
                .statusList(statusConverter(order.getOrderStatuses(), language))
                .time(order.getContainer().getCreatedAt())
                .build();
        return orderViewDTO;
    }

    private List<String> statusConverter(List<OrderStatus> orderStatuses, String language) {
//        TODO:
        List<String> statuses = new ArrayList<>();
        log.info(orderStatuses.size());
        for (OrderStatus orderStatus : orderStatuses) {
            Status status = orderStatus.getStatus();
            log.info("status {}", status);
            Language statusLanguage = status.getLanguage();
            log.info(statusLanguage.getChinese());
            log.info(statusLanguage.getEnglish());
            log.info(statusLanguage.getRussian());
            log.info(statusLanguage.getKazakh());
//            String acceptLanguage = switch (language) {
//                case "en" -> statusLanguage.getEnglish();
//                case "kk" -> statusLanguage.getKazakh();
//                case "ru" -> statusLanguage.getRussian();
//                case "cn" -> statusLanguage.getChinese();
//                default -> throw new IllegalStateException("Unexpected value: " + language);
//            };
            if (language.equals("en")) {
                statuses.add(statusLanguage.getEnglish());
            } else if(language.equals("ru")) {
                statuses.add(statusLanguage.getRussian());
            } else if(language.equals("cn")) {
                statuses.add(statusLanguage.getChinese());
            } else {
                statuses.add(statusLanguage.getKazakh());
            }


        }

        return statuses;
    }

    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}