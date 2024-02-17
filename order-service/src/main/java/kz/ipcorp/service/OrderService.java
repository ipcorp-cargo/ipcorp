package kz.ipcorp.service;

import kz.ipcorp.exception.NotConfirmedException;
import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.OrderCreateDTO;
import kz.ipcorp.model.DTO.OrderDetailDTO;
import kz.ipcorp.model.DTO.OrderUpdateStatus;
import kz.ipcorp.model.DTO.OrderViewDTO;
import kz.ipcorp.model.entity.*;
import kz.ipcorp.repository.OrderRepository;
import kz.ipcorp.repository.OrderStatusRepository;
import kz.ipcorp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final StatusService statusService;
    private final Logger log = LogManager.getLogger(OrderService.class);
    private final StatusRepository statusRepository;

    @Transactional
    public OrderViewDTO createOrder(OrderCreateDTO orderCreateDTO, String userId) {
        log.info("IN createOrder - orderName: {}, userId: {}", orderCreateDTO.getOrderName(), userId);

        if (orderRepository.existsByTrackCode(orderCreateDTO.getTrackCode())) {
            Order order = orderRepository.findByTrackCode(orderCreateDTO.getTrackCode()).get();
            order.setUserId(UUID.fromString(userId));
            order.setOrderName(orderCreateDTO.getOrderName());
            orderRepository.save(order);
            return OrderViewDTO.builder()
                    .id(order.getId())
                    .orderName(order.getOrderName())
                    .trackCode(order.getTrackCode())
                    .build();
        } else {
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

    }
    @Transactional(readOnly = true)
    public Order getById(UUID orderId){
        return orderRepository.findById(orderId).
                orElseThrow(() -> new NotFoundException("order not found"));
    }

    @Transactional
    public void deleteContainerFromOrder(UUID orderId){
        Order order = getById(orderId);
        order.setContainer(null);
        orderRepository.saveAndFlush(order);
    }

    @Transactional(readOnly = true)
    public Optional<Order> getByTrackCode(String trackCode) {
        log.info("IN getByTrackCode - trackCode: {}", trackCode);
        return orderRepository.findByTrackCode(trackCode);
    }

    @Transactional(readOnly = true)
    public List<OrderDetailDTO> getOrders(String userId, String language, Pageable pageable, UUID statusId) {
        log.info("IN getOrders - userId: {}", userId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));

        List<OrderDetailDTO> orders = new ArrayList<>();
        if (hasAdminRole) {
            Status status = statusRepository.findById(statusId).orElseThrow(() -> new NotFoundException(
                    String.format("status with id: %s not found", statusId)
            ));
            Page<Order> orderList = orderRepository.findAllByStatus(pageable, status);
            for (Order order : orderList) {
                orders.add(
                        OrderDetailDTO.builder()
                                .id(order.getId())
                                .orderName(order.getOrderName())
                                .trackCode(order.getTrackCode())
                                .statusList(statusConverter(order.getOrderStatuses(), language))
                                .build()
                );
            }
        } else {
            Page<Order> ordersList;

            if (statusId != null) {
                Status status = statusRepository.findById(statusId).orElseThrow(() -> new NotFoundException(
                        String.format("status with id: %s not found", statusId)
                ));
                ordersList = orderRepository.findAllByUserIdAndStatus(UUID.fromString(userId), pageable, status);
            } else {
                ordersList = orderRepository.findAllByUserId(UUID.fromString(userId), pageable);
            }
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
                        "status", statusLanguage.getEnglish(),
                        "time", orderStatus.getCreatedAt().toString()
                ));
                case "kk" -> statuses.add(Map.of(
                        "status", statusLanguage.getKazakh(),
                        "time", orderStatus.getCreatedAt().toString()
                ));
                case "ru" -> statuses.add(Map.of(
                        "status", statusLanguage.getRussian(),
                        "time", orderStatus.getCreatedAt().toString()
                ));
                case "cn" -> statuses.add(Map.of(
                        "status", statusLanguage.getChinese(),
                        "time", orderStatus.getCreatedAt().toString()
                ));
                default -> throw new IllegalStateException("Unexpected value: " + language);
            }
        }
        return statuses;
    }

    @Transactional
    public Order saveOrder(String trackCode) {
        if (orderRepository.existsByTrackCode(trackCode)) {
            throw new DuplicateFormatFlagsException(
                    String.format("order with track code already exists %s", trackCode)
            );
        }
        Order order = new Order();
        order.setTrackCode(trackCode);
        return orderRepository.save(order);
    }

    @Transactional
    public OrderDetailDTO updateStatus(Order order, Status status, String language) {
            createOrderStatus(order, status);
        Order updatedOrder = orderRepository.saveAndFlush(order);
        return OrderDetailDTO.builder()
                .id(updatedOrder.getId())
                .orderName(updatedOrder.getOrderName())
                .statusList(statusConverter(updatedOrder.getOrderStatuses(), language))
                .trackCode(updatedOrder.getTrackCode())
                .build();
    }

    @Transactional
    public OrderDetailDTO updateStatusPreLast(String trackCode, String language) {
        Order order = getByTrackCode(trackCode)
                .orElseThrow(() -> new NotFoundException(String.format("order with track code %s not found", trackCode)));
        if(!order.getStatus().getId().toString().equals("3a7b1136-2a5f-41a6-bedf-1f7968b2a781")){
            throw new NotConfirmedException("incorrect status");
        }

        Status status = statusService.findById(UUID.fromString("9a2f6db1-cf73-4d7b-bbf5-93632a8f070a"))
                .orElseThrow(() -> new NotFoundException(String.format("status with statusId %s not found", "9a2f6db1-cf73-4d7b-bbf5-93632a8f070a")));
        return updateStatus(order, status, language);
    }

    @Transactional
    public OrderDetailDTO updateStatusLast(String trackCode, String language) {
        Order order = getByTrackCode(trackCode)
                .orElseThrow(() -> new NotFoundException(String.format("order with track code %s not found", trackCode)));
        if(!order.getStatus().getId().toString().equals("9a2f6db1-cf73-4d7b-bbf5-93632a8f070a")){
            throw new NotConfirmedException("incorrect status");
        }

        Status status = statusService.findById(UUID.fromString("1cbd354c-341a-43ac-9136-94d2068896db"))
                .orElseThrow(() -> new NotFoundException(String.format("status with statusId %s not found", "9a2f6db1-cf73-4d7b-bbf5-93632a8f070a")));
        return updateStatus(order, status, language);
    }
}