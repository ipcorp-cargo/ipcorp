package kz.ipcorp.service;

import kz.ipcorp.exception.AuthenticationException;
import kz.ipcorp.exception.DuplicateEntityException;
import kz.ipcorp.exception.NotFoundException;
import kz.ipcorp.model.DTO.AccessTokenRequestDTO;
import kz.ipcorp.model.DTO.SignInRequestDTO;
import kz.ipcorp.model.DTO.SignUpRequestDTO;
import kz.ipcorp.model.DTO.TokenResponseDTO;
import kz.ipcorp.model.entity.User;
import kz.ipcorp.model.enumuration.Role;
import kz.ipcorp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final SMSVerificationService verificationService;

    private final JWTService jwtService;
    private final Logger log = LogManager.getLogger(AuthenticationService.class);


    static {
        System.out.println("AuthenticationService");
    }

    @Transactional
    public void createUser(SignUpRequestDTO signUpRequestDTO) {
        if(!verificationService.isVerificationCodeValid(
                signUpRequestDTO.getPhoneNumber(),
                signUpRequestDTO.getVerificationCode())){
            throw new AuthenticationException("sms verification code incorrect");
        }

        if (userRepository.findByPhoneNumber(signUpRequestDTO.getPhoneNumber()).isPresent()){
            throw new DuplicateEntityException("there is already a user with this number");
        }


        User user = new User();
        user.setPhoneNumber(signUpRequestDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        verificationService.invalidateVerificationCode(signUpRequestDTO.getPhoneNumber());
        log.info("IN createUser - phoneNumber: {}", signUpRequestDTO.getPhoneNumber());
    }

    public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        User user = userRepository.findByPhoneNumber(signInRequestDTO.getPhoneNumber())
                .orElseThrow(() -> new NotFoundException("user is not found"));

        var access = jwtService.generateToken(user);
        var refresh = jwtService.generateRefreshToken(new HashMap<>(), user);

        TokenResponseDTO tokens = new TokenResponseDTO();
        tokens.setAccessToken(access);
        tokens.setRefreshToken(refresh);
        log.info("IN signIn - phoneNumber: {}", signInRequestDTO.getPhoneNumber());
        return tokens;
    }

    public TokenResponseDTO accessToken(AccessTokenRequestDTO requestDTO) {
        UUID userId = UUID.fromString(jwtService.extractID(requestDTO.getRefreshToken()));
        User user = userRepository.findById(
                userId
        ).orElseThrow(() -> new NotFoundException("user is not found"));
        if (!jwtService.isTokenExpired(requestDTO.getRefreshToken())) {
            var access = jwtService.generateToken(user);
            log.info("IN accessToken - get access token with refresh token");
            TokenResponseDTO tokens = new TokenResponseDTO();
            tokens.setAccessToken(access);
            tokens.setRefreshToken(requestDTO.getRefreshToken());
            return tokens;
        }
        log.info("IN accessToken - can't get tokens, refresh token is valid");
        return null;
    }

    public void resetPassword(SignUpRequestDTO signUpRequestDTO){
        String newPassword = signUpRequestDTO.getPassword();
        String verificationCode = signUpRequestDTO.getVerificationCode();
        String phoneNumber = signUpRequestDTO.getPhoneNumber();

        if(!verificationService.isVerificationCodeValid(phoneNumber, verificationCode)){
            throw new AuthenticationException("sms verification code incorrect");
        }

        if (newPassword == null || newPassword.isEmpty()){
            throw new RuntimeException();
        }

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("user not found this phone number"));

        user.setPassword(passwordEncoder.encode(newPassword));
    }

}
