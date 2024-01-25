package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.ContainerCreateDTO;
import kz.ipcorp.model.DTO.ContainerReadDTO;
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
    private final Logger log = LogManager.getLogger(CategoryController.class);
    @GetMapping("/{containerId}")
    public ResponseEntity<ContainerReadDTO> getContainer(@PathVariable("containerId") UUID containerId){
        log.info("IN getContainer - containerId: {}", containerId.toString());
        return new ResponseEntity<>(containerService.getContainerById(containerId), HttpStatus.OK);
    }

    @GetMapping("/containerName")
    public ResponseEntity<ContainerReadDTO> getContainer(@RequestParam("containerName") String containerName){
        log.info("IN getContainer - containerName: {}", containerName);
        return new ResponseEntity<>(containerService.getContainerByName(containerName), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<HttpStatus> addContainer(@RequestBody ContainerCreateDTO containerCreateDTO){
        log.info("IN addContainer - containerName: {}", containerCreateDTO.getName());
        containerService.createContainer(containerCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{containerId}")
    public ResponseEntity<HttpStatus> addOrder(@RequestParam("orders") List<UUID> orders,
                                               @PathVariable("containerId") UUID containerId){
        log.info("IN addOrder - containerId: {}", containerId.toString());
        containerService.addOrder(orders, containerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ContainerReadDTO>> getAll(){
        log.info("IN getAll - get all containers");
        return new ResponseEntity<>(containerService.getAll(), HttpStatus.OK);
    }
}
