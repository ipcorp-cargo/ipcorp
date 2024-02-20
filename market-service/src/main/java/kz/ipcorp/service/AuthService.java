package kz.ipcorp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import java.util.HashMap;
import java.util.UUID;

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
        if (!verificationService.isVerificationCodeValid(
                sellerCreateDTO.getEmail(),
                sellerCreateDTO.getVerificationCode()
        )) {
            throw new AuthenticationException("sms verification code incorrect");
        }

        if (sellerRepository.findByEmail(sellerCreateDTO.getEmail()).isPresent()) {
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
    public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO, HttpServletResponse response, HttpServletRequest request) {
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

        ResponseCookie cookie = ResponseCookie.from("refresh-token", refresh)
                .domain("ipcorpn.com")
                .path("/api/auth/seller/access-token")
                .secure(true)
                .httpOnly(true)
                .sameSite("None")

                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return tokens;
    }

    @Transactional(readOnly = true)
    public TokenResponseDTO accessToken(HttpServletRequest request) {

        Cookie cookie = WebUtils.getCookie(request, "refresh-token");
        assert cookie != null;
        String refreshToken = cookie.getValue();

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AuthenticationException("refresh token not found");
        }

        UUID sellerId = UUID.fromString(jwtService.extractUsername(refreshToken));
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new NotFoundException("seller is not found"));

        log.info("IN accessToken - sellerEmail: {}", seller.getEmail());
        if (jwtService.isTokenValid(refreshToken)) {
            var access = jwtService.generateToken(seller);
            TokenResponseDTO tokens = new TokenResponseDTO();
            tokens.setAccessToken(access);
            return tokens;
        }
        return null;
    }

    @Transactional
    public void resetPassword(SellerCreateDTO sellerCreateDTO) {
        String newPassword = sellerCreateDTO.getPassword();
        String verificationCode = sellerCreateDTO.getVerificationCode();
        String email = sellerCreateDTO.getEmail();

        if (!verificationService.isVerificationCodeValid(email, verificationCode)) {
            throw new AuthenticationException("sms verification code incorrect");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new UserInputException("Password should not be empty");
        }

        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("user with email %s not found", email)));

        sellerRepository.save(seller);
        verificationService.invalidateVerificationCode(email);
    }

    public void logout() {



    }
}
