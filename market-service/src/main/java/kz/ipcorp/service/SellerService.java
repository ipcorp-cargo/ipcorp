package kz.ipcorp.service;

import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerService {

    private final SellerRepository sellerRepository;
    private final Logger log = LogManager.getLogger(SellerService.class);
    public Seller getById(UUID id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("seller with id %s not found", id)));
        log.info("IN getById - id: {}", id.toString());
        return seller;
    }


    public Seller getByEmail(String email) {
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException(String.format("seller with email %s not found", email)));
        return seller;
    }

    public UserDetailsService userDetailsService() {
        log.info("IN userDetailsService - find by email");
        return gmail -> sellerRepository.findByEmail(gmail)
                .orElseThrow(() -> new UsernameNotFoundException("seller not found"));
    }
}
