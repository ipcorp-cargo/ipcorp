package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.entity.Branch;
import kz.ipcorp.model.entity.User;
import kz.ipcorp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final Logger log = LogManager.getLogger(UserService.class);

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("user with id %s not found", id)));
    }

    public UserDetailsService userDetailsService() {
        log.info("IN userDetailsService - find by phone number");
        return phoneNumber -> userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException(
                        String.format("user with phoneNumber %s not found", phoneNumber)));
    }

    @Transactional
    public void addBranch(Branch branch, UUID userId){
        User user = findById(userId);
        user.setBranch(branch);
        userRepository.saveAndFlush(user);
    }

}
