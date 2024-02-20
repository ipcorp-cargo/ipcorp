package kz.ipcorp.controller;

import jakarta.ws.rs.Path;
import kz.ipcorp.model.DTO.OrderUpdateStatus;
import kz.ipcorp.model.DTO.OrderCreateDTO;
import kz.ipcorp.model.DTO.OrderDetailDTO;
import kz.ipcorp.model.DTO.OrderViewDTO;
import kz.ipcorp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final Logger log = LogManager.getLogger(OrderController.class);

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<OrderViewDTO> createOrder(@RequestBody OrderCreateDTO orderCreateDTO,
                                                    Principal principal) {
        log.info("IN createOrder - userId: {}, orderName: {}", principal.getName(), orderCreateDTO.getOrderName());
        return new ResponseEntity<>(orderService.createOrder(orderCreateDTO, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<OrderDetailDTO>> getOrders(@CookieValue(name = "Accept-Language", defaultValue = "ru") String language,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "10") int size,
                                                          @RequestParam(value = "statusId", required = false) UUID statusId,
                                                          Principal principal) {
        String userId = principal.getName();
        log.info("IN getOrders - userId: {}", userId);
        return new ResponseEntity<>(orderService.getOrders(userId, language, PageRequest.of(page, size), statusId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/status")
    public ResponseEntity<OrderDetailDTO> updateStatusOrderByTrackCodePreLast(@RequestParam("trackCode") String trackCode,
                                                                       @CookieValue(name = "Accept-Language", defaultValue = "ru") String language){
        log.info("IN updateStatusOrderByTrackCodePreLast - orderTrackCode: {}", trackCode);
        return new ResponseEntity<>(orderService.updateStatusPreLast(trackCode, language),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/status/last")
    public ResponseEntity<OrderDetailDTO> updateStatusOrderByTrackCodeToLast(@RequestParam("trackCode") String trackCode,
                                                                       @CookieValue(name = "Accept-Language", defaultValue = "ru") String language){
        log.info("IN updateStatusOrderByTrackCodeLast - orderTrackCode: {}", trackCode);
        return new ResponseEntity<>(orderService.updateStatusLast(trackCode, language),HttpStatus.OK);
    }


    @GetMapping("/{trackCode}")
    public ResponseEntity<OrderDetailDTO> getOrder(@PathVariable("trackCode") String trackCode,
                                                   @CookieValue(name = "Accept-Language", defaultValue = "ru") String language) {
        log.info("IN getOrder - trackCode: {}, language: {}", trackCode, language);
        return ResponseEntity.ok(orderService.getOrder(trackCode, language));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("orderId") UUID orderId,
                                                  Principal principal) {
        orderService.deleteOrder(orderId, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
