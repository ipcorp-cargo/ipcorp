package kz.ipcorp.service;

import kz.ipcorp.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerService {

    private final SellerRepository sellerRepository;

    public UserDetailsService userDetailsService(){
        return gmail -> sellerRepository.findByEmail(gmail)
                .orElseThrow(() -> new UsernameNotFoundException("seller not found"));
    }
}
