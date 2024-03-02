package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.feign.UserFeignClient;
import kz.ipcorp.model.DTO.ProductIdsWrapper;
import kz.ipcorp.model.DTO.ProductViewDTO;
import kz.ipcorp.model.entity.Branch;
import kz.ipcorp.model.entity.User;
import kz.ipcorp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserFeignClient userFeignClient;
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

    @Transactional
    public void addFavoriteProduct(UUID userId, UUID productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        user.getFavoriteProducts().add(productId);
        userRepository.saveAndFlush(user);
    }

    public ResponseEntity<List<ProductViewDTO>> getFavoriteProducts(UUID userId, String language) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        return userFeignClient.getFavoriteProducts(language, user.getFavoriteProducts());
    }

    @Transactional
    public void deleteFavoriteProduct(UUID userId, UUID productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        user.removeFavoriteProduct(productId);
        userRepository.saveAndFlush(user);
    }
}
