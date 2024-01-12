package kz.ipcorp.service;

import kz.ipcorp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;



    public UserDetailsService userDetailsService() {
        return phoneNumber -> userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

}
