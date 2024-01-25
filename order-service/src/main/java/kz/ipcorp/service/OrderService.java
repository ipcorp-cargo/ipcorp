package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.OrderCreateDTO;
import kz.ipcorp.model.DTO.OrderViewDTO;
import kz.ipcorp.model.entity.Container;
import kz.ipcorp.model.entity.Order;
import kz.ipcorp.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final Logger log = LogManager.getLogger(OrderService.class);

    @Transactional(readOnly = true)
    public Order getById(UUID orderId) {
        log.info("IN getById - orderId: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("container with orderId %s not found", orderId)));
    }

    @Transactional
    public OrderViewDTO createOrder(OrderCreateDTO orderCreateDTO, String userId) {
        log.info("IN createOrder - orderName: {}, userId: {}", orderCreateDTO.getOrderName(), userId);
        Order order = new Order();
        order.setTrackCode(orderCreateDTO.getTrackCode());
        order.setOrderName(orderCreateDTO.getOrderName());
        order.setUserId(UUID.fromString(userId));
        orderRepository.save(order);
        return new OrderViewDTO(order);
    }

    @Transactional(readOnly = true)
    public List<OrderViewDTO> getOrders(String userId) {
        log.info("IN getOrders - userId: {}", userId);
        List<Order> ordersList = orderRepository.findAllByUserId(UUID.fromString(userId));
        List<OrderViewDTO> orders = new ArrayList<>();
        for (Order order : ordersList) {
            orders.add(new OrderViewDTO(order));
        }
        return orders;
    }

    @Transactional
    public void addContainer(Order order, Container container) {
        log.info("IN addContainer - orderName: {}, containerName: {}", order.getOrderName(), container.getName());
        order.setContainer(container);
        orderRepository.saveAndFlush(order);
    }
}
