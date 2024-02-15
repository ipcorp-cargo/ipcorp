package kz.ipcorp.service;

import kz.ipcorp.exception.DuplicatedEntityException;
import kz.ipcorp.exception.NotConfirmedException;
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

        if (containerRepository.existsByName(createDTO.getName())) {
            throw new DuplicatedEntityException(String.format("container with name: '%s' already exists", createDTO.getName()));
        }

        Container container = new Container();
        container.setName(createDTO.getName());
        container.setOrders(new ArrayList<>());

        Status status = statusService.findById(UUID.fromString("7050543b-b370-4dfd-bc6b-888542aa26ca"))
                .orElseThrow(() -> new NotFoundException("nurs statusti durst-au kerek"));
        container.setStatus(status);
        containerRepository.save(container);
        return new ContainerReadDTO(container);
    }

    @Transactional
    public ContainerReadDTO addOrder(UUID containerId, String trackCode) {
        log.info("IN addOrder - containerId: {}", containerId);

        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new NotFoundException(String.format("container with containerId %s not found", containerId)));
        Order order = orderService.getByTrackCode(trackCode).orElseGet(() -> orderService.saveOrder(trackCode));
        container.getOrders().add(order);
        Container savedContainer = containerRepository.saveAndFlush(container);
        orderService.addContainer(order, savedContainer);
        orderService.createOrderStatus(order, container.getStatus());
        return new ContainerReadDTO(container);
    }

    public ContainerReadDTO getContainerByName(String containerName) {
        log.info("IN getContainerByName - containerName: {}", containerName);
        Container container = containerRepository.findContainerByName(containerName).
                orElseThrow(()
                        -> new NotFoundException(String.format("container with name %s not found", containerName)));
        return new ContainerReadDTO(container);
    }

    public ContainerDetailDTO getContainerById(UUID containerId, String language) {
        log.info("IN getContainerById - containerId: {}", containerId);
        Container container = containerRepository.findById(containerId).
                orElseThrow(()
                        -> new NotFoundException(String.format("container with containerId %s not found", containerId)));
        return new ContainerDetailDTO(container, language);
    }

    public List<ContainerReadDTO> getAll() {
        log.info("IN getAll - get all containers");
        List<Container> containers = containerRepository.findAll();
        List<ContainerReadDTO> containerReadDTOList = new ArrayList<>();
        for (Container container : containers) {
            containerReadDTOList.add(new ContainerReadDTO(container));
        }
        return containerReadDTOList;
    }

    @Transactional
    public void updateContainerStatus(UUID containerId, ContainerStatusDTO containerStatusDTO) {
        log.info("IN updateContainerStatus - containerId: {}, statusId: {}", containerId, containerStatusDTO.getStatusId());
        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new NotFoundException("container not found"));
        Status status = statusService.findById(containerStatusDTO.getStatusId())
                .orElseThrow(() -> new NotFoundException("status not found"));
        container.setStatus(status);
        for (Order order : container.getOrders()) {
            orderService.createOrderStatus(order, status);
            log.info("order: {}", order.getOrderName());
        }
        containerRepository.save(container);
    }

    @Transactional
    public void deleteOrderFromContainer(UUID containerId, UUID orderId) {
        try {
            containerRepository.deleteOrderFromContainer(containerId, orderId);
        } catch (Exception e) {
            throw new NotFoundException("container or order not found, order is not in this container");
        }
    }

    @Transactional
    public void deleteContainer(UUID containerId) {
        Container container = containerRepository.findById(containerId).orElseThrow(() ->
                new NotFoundException("container not found"));

        if (!container.getOrders().isEmpty()) {
            throw new NotConfirmedException("container is not empty");
        }

        containerRepository.delete(container);

    }
}