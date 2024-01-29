package kz.ipcorp.service;

import kz.ipcorp.model.entity.Status;
import kz.ipcorp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
    public Optional<Status> findById(UUID statusId) {
        return statusRepository.findById(statusId);
    }
}
