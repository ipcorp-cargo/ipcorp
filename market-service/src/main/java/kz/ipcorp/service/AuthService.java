package kz.ipcorp.service;

import kz.ipcorp.exception.NotConfirmedException;
import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.*;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.model.entity.Verification;
import kz.ipcorp.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SellerRepository sellerRepository;
    private final VerificationService verificationService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final Logger log = LogManager.getLogger(AuthService.class);
    @Transactional
    public void registerSeller(SellerCreateDTO sellerCreateDTO) {
        Seller seller = new Seller();
        seller.setEmail(sellerCreateDTO.getEmail());
        seller.setPassword(passwordEncoder.encode(sellerCreateDTO.getPassword()));
        sellerRepository.save(seller);
        verificationService.registerCode(seller);
        log.info("IN registerSeller - sellerEmail: {}", seller.getEmail());
    }



    @Transactional
    public void confirmSeller(SellerConfirmDTO sellerConfirmDTO) {
        Seller seller = sellerRepository.findByEmail(sellerConfirmDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("seller isn't registered"));
        verificationService.checkCode(seller, sellerConfirmDTO);
        log.info("IN confirmSeller - sellerEmail: {}", seller.getEmail());
    }

    @Transactional(readOnly = true)
    public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        var seller = sellerRepository.findByEmail(signInRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("seller is not found"));
        log.info("IN signIn - sellerEmail: {}", seller.getEmail());
        Verification verification = verificationService.getVerification(seller);
        if (!verification.getIsConfirmed()) {
            log.error("IN signIn - Seller hasn't confirmed, sellerStatus: {}", false);
            throw new NotConfirmedException("seller is not confirmed");
        }
        var access = jwtService.generateToken(seller);
        var refresh = jwtService.generateRefreshToken(new HashMap<>(), seller);

        TokenResponseDTO tokens = new TokenResponseDTO();
        tokens.setAccessToken(access);
        tokens.setRefreshToken(refresh);

        return tokens;
    }

    @Transactional(readOnly = true)
    public TokenResponseDTO accessToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("seller is not found"));
        log.info("IN accessToken - sellerEmail: {}", seller.getEmail());
        if (jwtService.isTokenValid(refreshToken)) {
            var access = jwtService.generateToken(seller);

            TokenResponseDTO tokens = new TokenResponseDTO();
            tokens.setAccessToken(access);
            tokens.setRefreshToken(refreshToken);
            return tokens;
        }
        return null;
    }
}
