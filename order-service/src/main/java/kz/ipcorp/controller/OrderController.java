package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.OrderCreateDTO;
import kz.ipcorp.model.DTO.OrderViewDTO;
import kz.ipcorp.model.entity.Order;
import kz.ipcorp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderViewDTO> createOrder(@RequestBody OrderCreateDTO orderCreateDTO, @RequestHeader(value = "userId") String userId) {
        return new ResponseEntity<>(orderService.createOrder(orderCreateDTO, userId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderViewDTO>> getOrders(@RequestHeader(value = "userId", required = false) String userId) {
        return new ResponseEntity<>(orderService.getOrders(userId), HttpStatus.OK);
    }

}
