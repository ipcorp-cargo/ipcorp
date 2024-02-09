package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.OrderCreateDTO;
import kz.ipcorp.model.DTO.OrderDetailDTO;
import kz.ipcorp.model.DTO.OrderViewDTO;
import kz.ipcorp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final Logger log = LogManager.getLogger(OrderController.class);

    @PostMapping
    public ResponseEntity<OrderViewDTO> createOrder(@RequestBody OrderCreateDTO orderCreateDTO,
                                                    @RequestHeader(value = "userId") String userId) {
        log.info("IN createOrder - userId: {}, orderName: {}", userId, orderCreateDTO.getOrderName());
        return new ResponseEntity<>(orderService.createOrder(orderCreateDTO, userId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailDTO>> getOrders(@RequestHeader(value = "userId", required = false) String userId,
                                                          @CookieValue(name = "Accept-Language", defaultValue = "ru") String language) {
        log.info("IN getOrders - userId: {}", userId);
        return new ResponseEntity<>(orderService.getOrders(userId, language), HttpStatus.OK);
    }


    @GetMapping("/{trackCode}")
    public ResponseEntity<OrderDetailDTO> getOrder(@PathVariable("trackCode") String trackCode,
                                                   @CookieValue(name = "Accept-Language", defaultValue = "ru") String language) {
        log.info("IN getOrder - trackCode: {}, language: {}", trackCode, language);
        return ResponseEntity.ok(orderService.getOrder(trackCode, language));
    }
}
