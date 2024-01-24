package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.entity.User;
import kz.ipcorp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("user not found with id %s", id))
        );
    }

    public UserDetailsService userDetailsService() {
        return phoneNumber -> userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("user is not found"));
    }

}
