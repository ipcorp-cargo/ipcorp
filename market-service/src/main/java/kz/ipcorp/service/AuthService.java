package kz.ipcorp.service;

import kz.ipcorp.exception.AuthenticationException;
import kz.ipcorp.exception.DuplicateEntityException;
import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.exception.UserInputException;
import kz.ipcorp.model.DTO.*;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
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
    private final EmailVerificationService verificationService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final Logger log = LogManager.getLogger(AuthService.class);

    @Transactional
    public void createSeller(SellerCreateDTO sellerCreateDTO) {
        if(!verificationService.isVerificationCodeValid(
                sellerCreateDTO.getEmail(),
                sellerCreateDTO.getVerificationCode()
        )){
            throw new AuthenticationException("sms verification code incorrect");
        }

        if (sellerRepository.findByEmail(sellerCreateDTO.getEmail()).isPresent()){
            throw new DuplicateEntityException("there is already a seller with email");
        }

        Seller seller = new Seller();
        seller.setEmail(sellerCreateDTO.getEmail());
        seller.setPassword(passwordEncoder.encode(sellerCreateDTO.getPassword()));
        sellerRepository.save(seller);
        verificationService.invalidateVerificationCode(sellerCreateDTO.getEmail());
        log.info("IN registerSeller - sellerEmail: {}", seller.getEmail());
    }

    @Transactional(readOnly = true)
    public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        var seller = sellerRepository.findByEmail(signInRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(
                        String.format("seller with email %s not found", signInRequestDTO.getEmail())));
        log.info("seller password {}", seller.getPassword());
        log.info("signInRequestDTO password {}", signInRequestDTO.getPassword());
        if (!passwordEncoder.matches(signInRequestDTO.getPassword(), seller.getPassword())) {
            throw new AuthenticationException("email or password incorrect");
        }
        log.info("IN signIn - sellerEmail: {}", seller.getEmail());
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
        Seller seller = sellerRepository.findByEmail(email).
                orElseThrow(() -> new NotFoundException(
                        String.format("seller with email %s not found", email)));
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

    @Transactional
    public void resetPassword(SellerCreateDTO sellerCreateDTO){
        String newPassword = sellerCreateDTO.getPassword();
        String verificationCode = sellerCreateDTO.getVerificationCode();
        String email = sellerCreateDTO.getEmail();

        if(!verificationService.isVerificationCodeValid(email, verificationCode)){
            throw new AuthenticationException("sms verification code incorrect");
        }

        if(newPassword == null || newPassword.isEmpty()){
            throw new UserInputException("Password should not be empty");
        }

        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("user with email %s not found", email)));

        seller.setPassword(passwordEncoder.encode(newPassword));
        sellerRepository.save(seller);
        verificationService.invalidateVerificationCode(email);
    }
}
