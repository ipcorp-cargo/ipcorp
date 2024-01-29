package kz.ipcorp.service;

import kz.ipcorp.exception.DuplicatedEntityException;
import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.*;
import kz.ipcorp.model.entity.Container;
import kz.ipcorp.model.entity.Order;
import kz.ipcorp.model.entity.Status;
import kz.ipcorp.repository.ContainerRepository;
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
@Transactional(readOnly = true)
public class ContainerService {
    private final ContainerRepository containerRepository;
    private final OrderService orderService;
    private final StatusService statusService;
    private final Logger log = LogManager.getLogger(ContainerService.class);

    @Transactional
    public ContainerReadDTO createContainer(ContainerCreateDTO createDTO) {
        log.info("IN createContainer - containerName: {}", createDTO.getName());
        boolean isContainerExists = containerRepository.existsByName(createDTO.getName());
        if (isContainerExists) {
            throw new DuplicatedEntityException(
                    String.format("container with name: %s already exists", createDTO.getName()
                    ));
        }
        Container container = new Container();
        container.setName(createDTO.getName());
        container.setOrders(new ArrayList<>());
        containerRepository.save(container);
        return new ContainerReadDTO(container);
    }

    @Transactional
    public ContainerReadDTO addOrder(UUID containerId, ContainerOrderCreateDTO containerOrderCreateDTO) {
        log.info("IN addOrder - containerId: {}", containerId);
        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new NotFoundException(String.format("container with containerId %s not found", containerId)));
        List<Order> orders = container.getOrders();
        for (String trackCode : containerOrderCreateDTO.getOrderTrackCodes()) {
            Order order = orderService.getByTrackCode(trackCode);
            orders.add(order);
            orderService.addContainer(order, container);
        }
        container.setOrders(orders);
        containerRepository.saveAndFlush(container);
        return new ContainerReadDTO(container);
    }

    public ContainerReadDTO getContainerByName(String containerName) {
        log.info("IN getContainerByName - containerName: {}", containerName);
        Container container = containerRepository.findContainerByName(containerName).
                orElseThrow(()
                        -> new NotFoundException(String.format("container with name %s not found", containerName)));
        return new ContainerReadDTO(container);
    }

    public ContainerDetailDTO getContainerById(UUID containerId) {
        log.info("IN getContainerById - containerId: {}", containerId);
        Container container = containerRepository.findById(containerId).
                orElseThrow(()
                        -> new NotFoundException(String.format("container with containerId %s not found", containerId)));
        return new ContainerDetailDTO(container);
    }

    public List<ContainerReadDTO> getAll() {
        log.info("IN getAll - get all containers");
        List<Container> containers = containerRepository.findAll();
        List<ContainerReadDTO> containerReadDTOList = new ArrayList<>();
        for (Container c : containers) {
            containerReadDTOList.add(new ContainerReadDTO(c));
        }
        return containerReadDTOList;
    }

    @Transactional
    public void updateContainerStatus(UUID containerId, ContainerStatusDTO containerStatusDTO) {
        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new NotFoundException("container not found"));
        Status status = statusService.findById(containerStatusDTO.getStatusId())
                .orElseThrow(() -> new NotFoundException("status not found"));
        container.setStatus(status);
        for (Order order : container.getOrders()) {
            orderService.updateOrderStatus(order, status);
            log.info("order: {}", order.getOrderName());
        }
        containerRepository.save(container);
    }
}