package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.StatusViewDTO;
import kz.ipcorp.model.entity.Status;
import kz.ipcorp.model.entity.User;
import kz.ipcorp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
    private final UserService userService;
    @Transactional(readOnly = true)
    public List<StatusViewDTO> getStatus(UUID userId){
        User user = userService.findById(userId);
        List<StatusViewDTO> statuses = new ArrayList<>();
        for (Status status : user.getStatuses()) {
            statuses.add(new StatusViewDTO(status));
        }
        return statuses;
    }
}