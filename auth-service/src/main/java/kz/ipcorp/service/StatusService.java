package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.entity.Status;
import kz.ipcorp.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;

    public List<Status> getStatus(){
        List<Status> statuses = statusRepository.findAll();
        if(statuses.isEmpty()){
            throw new NotFoundException("not status");
        }
        return statusRepository.findAll();
    }
}