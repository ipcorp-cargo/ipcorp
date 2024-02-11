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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
        return OrderViewDTO.builder()
                .id(order.getId())
                .orderName(order.getOrderName())
                .trackCode(order.getTrackCode())
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<Order> getByTrackCode(String trackCode) {
        log.info("IN getByTrackCode - trackCode: {}", trackCode);
        return orderRepository.findByTrackCode(trackCode);
    }

    @Transactional(readOnly = true)
    public List<OrderDetailDTO> getOrders(String userId, String language, Pageable pageable) {
        log.info("IN getOrders - userId: {}", userId);
        Page<Order> ordersList = orderRepository.findAllByUserId(UUID.fromString(userId), pageable);
        List<OrderDetailDTO> orders = new ArrayList<>();
        for (Order order : ordersList) {
            orders.add(
                    OrderDetailDTO.builder()
                            .id(order.getId())
                            .orderName(order.getOrderName())
                            .trackCode(order.getTrackCode())
                            .statusList(statusConverter(order.getOrderStatuses(), language))
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
    public void createOrderStatus(Order order, Status status) {
        log.info("IN addStatus - orderId: {}, statusId: {}", order.getId(), status.getId());
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus(status);
        orderStatus.setOrder(order);
        orderStatusRepository.save(orderStatus);

        List<OrderStatus> orderStatuses = order.getOrderStatuses();
        orderStatuses.add(orderStatus);
        order.setOrderStatuses(orderStatuses);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public OrderDetailDTO getOrder(String trackCode, String language) {
        log.info("IN getOrder {} {}", trackCode, language);
        Order order = orderRepository.findByTrackCode(trackCode).orElseThrow(
                () -> new NotFoundException("order not found"));

        return OrderDetailDTO.builder()
                .id(order.getId())
                .orderName(order.getOrderName())
                .trackCode(order.getTrackCode())
                .statusList(statusConverter(order.getOrderStatuses(), language))
                .build();
    }

    private List<Map<String, String>> statusConverter(List<OrderStatus> orderStatuses, String language) {
        List<Map<String, String>> statuses = new ArrayList<>();
        log.info("language: {}", language);
        for (OrderStatus orderStatus : orderStatuses) {
            Status status = orderStatus.getStatus();
            log.info("status {}", status);
            Language statusLanguage = status.getLanguage();
            switch (language) {
                case "en" -> statuses.add(Map.of(
                        "status" , statusLanguage.getEnglish(),
                        "time" , orderStatus.getCreatedAt().toString()
                ));
                case "kk" -> statuses.add(Map.of(
                        "status" , statusLanguage.getKazakh(),
                        "time" , orderStatus.getCreatedAt().toString()
                ));
                case "ru" -> statuses.add(Map.of(
                        "status" , statusLanguage.getRussian(),
                        "time" , orderStatus.getCreatedAt().toString()
                ));
                case "cn" -> statuses.add(Map.of(
                        "status" , statusLanguage.getChinese(),
                        "time" , orderStatus.getCreatedAt().toString()
                ));
                default -> throw new IllegalStateException("Unexpected value: " + language);
            }
        }
        return statuses;
    }

    @Transactional
    public Order saveOrder(String trackCode) {
        Order order = new Order();
        order.setTrackCode(trackCode);
        return orderRepository.save(order);
    }
}