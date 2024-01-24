package kz.ipcorp.service;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;
    private final Logger log = LogManager.getLogger(AuthenticationService.class);


    static {
        System.out.println("AuthenticationService");
    }

    @Transactional
    public User createUser(SignUpRequestDTO signUpRequestDTO) {
        User user = new User();
        user.setPhoneNumber(signUpRequestDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        log.info("IN createUser - phoneNumber: {}", signUpRequestDTO.getPhoneNumber());
        return savedUser;
    }

    public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequestDTO.getPhoneNumber(), signInRequestDTO.getPassword()
        ));
        System.out.println(authenticate.getPrincipal());
        var user = userRepository.findByPhoneNumber(signInRequestDTO.getPhoneNumber())
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
        String phoneNumber = jwtService.extractUsername(requestDTO.getRefreshToken());
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new NotFoundException("user is not found"));
        if (jwtService.isTokenValid(requestDTO.getRefreshToken(), user)) {
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

}
