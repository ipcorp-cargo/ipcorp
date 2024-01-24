package kz.ipcorp.service;

import kz.ipcorp.model.DTO.OrderCreateDTO;
import kz.ipcorp.model.DTO.OrderViewDTO;
import kz.ipcorp.model.entity.Order;
import kz.ipcorp.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public OrderViewDTO createOrder(OrderCreateDTO orderCreateDTO, String userId) {
        Order order = new Order();
        order.setTrackCode(orderCreateDTO.getTrackCode());
        order.setOrderName(orderCreateDTO.getOrderName());
        order.setUserId(UUID.fromString(userId));
        orderRepository.save(order);
        return new OrderViewDTO(order);
    }

    @Transactional(readOnly = true)
    public List<OrderViewDTO> getOrders(String userId) {
        List<Order> ordersList = orderRepository.findAllByUserId(UUID.fromString(userId));

        List<OrderViewDTO> orders = new ArrayList<>();

        for (Order order : ordersList) {
            orders.add(new OrderViewDTO(order));
        }

        return orders;
    }
}
