package kz.ipcorp.service;

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
import java.util.Optional;

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
                .orElseThrow(() -> new IllegalArgumentException("something wrong"));
        verificationService.checkCode(seller, sellerConfirmDTO);
    }

    @Transactional(readOnly = true)
    public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequestDTO.getEmail(), signInRequestDTO.getPassword()
        ));
        System.out.println(authenticate.getPrincipal());
        var user = sellerRepository.findByEmail(signInRequestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("email or password is incorrect!"));
        Verification verification = verificationService.getVerification(user);
        if (!verification.getIsConfirmed()) {
            throw new IllegalArgumentException("seller is not registered");
        }
        var access = jwtService.generateToken(user);
        var refresh = jwtService.generateRefreshToken(new HashMap<>(), user);

        TokenResponseDTO tokens = new TokenResponseDTO();
        tokens.setAccessToken(access);
        tokens.setRefreshToken(refresh);

        return tokens;
    }

    @Transactional(readOnly = true)
    public TokenResponseDTO accessToken(AccessTokenRequestDTO requestDTO) {
        String email = jwtService.extractUsername(requestDTO.getRefreshToken());
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("phone number is incorrect"));
        if (jwtService.isTokenValid(requestDTO.getRefreshToken(), seller)) {
            var access = jwtService.generateToken(seller);

            TokenResponseDTO tokens = new TokenResponseDTO();
            tokens.setAccessToken(access);
            tokens.setRefreshToken(requestDTO.getRefreshToken());
            return tokens;
        }
        return null;
    }
}
