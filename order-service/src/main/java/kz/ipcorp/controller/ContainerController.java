package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.*;
import kz.ipcorp.service.ContainerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/containers")
@RequiredArgsConstructor
public class ContainerController {
    private final ContainerService containerService;
    private final Logger log = LogManager.getLogger(ContainerController.class);

    @GetMapping("/{containerId}")
    public ResponseEntity<ContainerDetailDTO> getContainer(@PathVariable("containerId") UUID containerId) {
        log.info("IN getContainer - containerId: {}", containerId.toString());
        return new ResponseEntity<>(containerService.getContainerById(containerId), HttpStatus.OK);
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
    public ResponseEntity<ContainerReadDTO> addOrder(@RequestHeader(value = "userId", required = false) UUID userID, @PathVariable("containerId") UUID containerId,
                                                     @RequestBody ContainerOrderCreateDTO containerOrderCreateDTO) {
        log.info("userID - {}", userID);
        log.info("IN addOrder - containerId: {}", containerId);
        return new ResponseEntity<>(containerService.addOrder(containerId, containerOrderCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ContainerReadDTO>> getAll() {
        log.info("IN getAll - get all containers");
        return new ResponseEntity<>(containerService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/{containerId}/status")
    public ResponseEntity<HttpStatus> addStatus(@PathVariable("containerId") UUID containerId,
                                                @RequestBody ContainerStatusDTO containerStatusDTO
                                                ) {
        containerService.updateContainerStatus(containerId, containerStatusDTO);
        log.info("che tam");
        log.info("IN addStatus - containerId: {}", containerId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
