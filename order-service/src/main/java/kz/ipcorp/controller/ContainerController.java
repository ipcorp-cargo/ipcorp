package kz.ipcorp.controller;

import jakarta.ws.rs.Path;
import kz.ipcorp.model.DTO.*;
import kz.ipcorp.service.ContainerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/containers")
@RequiredArgsConstructor
@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
public class ContainerController {
    private final ContainerService containerService;
    private final Logger log = LogManager.getLogger(ContainerController.class);

    @GetMapping("/{containerId}")
    public ResponseEntity<ContainerDetailDTO> getContainer(@PathVariable("containerId") UUID containerId,
                                                           @CookieValue(name = "Accept-Language") String language) {
        log.info("IN getContainer - containerId: {}", containerId.toString());
        return new ResponseEntity<>(containerService.getContainerById(containerId, language), HttpStatus.OK);
    }

    @GetMapping("/containerName")
    public ResponseEntity<ContainerReadDTO> getContainer(@RequestParam("containerName") String containerName) {
        log.info("IN getContainer - containerName: {}", containerName);
        return new ResponseEntity<>(containerService.getContainerByName(containerName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContainerReadDTO> addContainer(@RequestBody ContainerCreateDTO containerCreateDTO) {
        log.info("IN addContainer - containerName: {}", containerCreateDTO.getName());
        return new ResponseEntity<>(containerService.createContainer(containerCreateDTO), HttpStatus.CREATED);
    }


    @PostMapping("/{containerId}/order")
    public ResponseEntity<ContainerReadDTO> addOrder(@PathVariable("containerId") UUID containerId,
                                                     @RequestBody ContainerOrderCreateDTO containerOrderCreateDTO) {
        log.info("IN addOrder - containerId: {}", containerId);
        return new ResponseEntity<>(containerService.addOrder(containerId, containerOrderCreateDTO.getTrackCode()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ContainerReadDTO>> getAll() {
        log.info("IN getAll - get all containers");
        return new ResponseEntity<>(containerService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/{containerId}/status")
    public ResponseEntity<HttpStatus> addStatus(@PathVariable("containerId") UUID containerId,
                                                @RequestBody ContainerStatusDTO containerStatusDTO) {
        log.info("IN addStatus - containerId: {}", containerId);
        containerService.updateContainerStatus(containerId, containerStatusDTO);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{containerId}/{orderId}")
    public ResponseEntity<HttpStatus> deleteOrderFromContainer(@PathVariable("containerId") UUID containerId,
                                                               @PathVariable("orderId") UUID orderId) {
        containerService.deleteOrderFromContainer(containerId, orderId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/{containerId}")
    public ResponseEntity<HttpStatus> deleteContainer(@PathVariable("containerId") UUID containerId) {
        containerService.deleteContainer(containerId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
