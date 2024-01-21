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
    @Transactional
    public void registerSeller(SellerCreateDTO sellerCreateDTO) {
        Seller seller = new Seller();
        seller.setEmail(sellerCreateDTO.getEmail());
        seller.setPassword(passwordEncoder.encode(sellerCreateDTO.getPassword()));
        sellerRepository.save(seller);
        verificationService.registerCode(seller);
    }



    @Transactional
    public void confirmSeller(SellerConfirmDTO sellerConfirmDTO) {
        Seller seller = sellerRepository.findByEmail(sellerConfirmDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("seller isn't registered"));
        verificationService.checkCode(seller, sellerConfirmDTO);
    }

    @Transactional(readOnly = true)
    public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                signInRequestDTO.getEmail(), signInRequestDTO.getPassword()
//        ));
        var seller = sellerRepository.findByEmail(signInRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("seller is not found"));
        Verification verification = verificationService.getVerification(seller);
        if (!verification.getIsConfirmed()) {
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
