package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.ContainerCreateDTO;
import kz.ipcorp.model.DTO.ContainerReadDTO;
import kz.ipcorp.service.ContainerService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{containerId}")
    public ResponseEntity<ContainerReadDTO> getContainer(@PathVariable("containerId") UUID containerId){
        return new ResponseEntity<>(containerService.getContainerById(containerId), HttpStatus.OK);
    }

    @GetMapping("/containerName")
    public ResponseEntity<ContainerReadDTO> getContainer(@RequestParam("containerName") String containerName){
        return new ResponseEntity<>(containerService.getContainerByName(containerName), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<HttpStatus> addContainer(@RequestBody ContainerCreateDTO containerCreateDTO){
        containerService.createContainer(containerCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> addOrder(@RequestParam("orders") List<UUID> orders,
                                               @PathVariable("id") UUID id){
        containerService.addOrder(orders, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ContainerReadDTO>> getAll(){
        return new ResponseEntity<>(containerService.getAll(), HttpStatus.OK);
    }
}
