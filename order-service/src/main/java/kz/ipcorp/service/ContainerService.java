package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.ContainerCreateDTO;
import kz.ipcorp.model.DTO.ContainerReadDTO;
import kz.ipcorp.model.entity.Container;
import kz.ipcorp.model.entity.Order;
import kz.ipcorp.repository.ContainerRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void createContainer(ContainerCreateDTO  createDTO){
        Container container = new Container();
        container.setName(createDTO.getName());
        containerRepository.save(container);
    }

    @Transactional
    public void addOrder(List<UUID> ordersId, UUID id){
        Container container = containerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("container with id %s not found", id)));
        List<Order> orders = container.getOrders();
        for(UUID orderId : ordersId){
            Order order = orderService.getById(orderId);
            orders.add(order);
            orderService.addContainer(order, container);
        }
        container.setOrders(orders);
        containerRepository.saveAndFlush(container);
    }
    public ContainerReadDTO getContainerByName(String containerName){
        Container container = containerRepository.findContainerByName(containerName).
                orElseThrow(()
                        -> new NotFoundException(String.format("container with name %s not found", containerName)));
        return new ContainerReadDTO(container);
    }
    public ContainerReadDTO getContainerById(UUID id){
        Container container = containerRepository.findById(id).
                orElseThrow(()
                        -> new NotFoundException(String.format("container with id %s not found", id)));
        return new ContainerReadDTO(container);
    }

    public List<ContainerReadDTO> getAll(){
        List<Container> containers = containerRepository.findAll();
        List<ContainerReadDTO> containerReadDTOList = new ArrayList<>();
        for(Container c : containers){
            containerReadDTOList.add(new ContainerReadDTO(c));
        }
        return containerReadDTOList;
    }
}