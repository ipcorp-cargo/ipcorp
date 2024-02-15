package kz.ipcorp.service;

import kz.ipcorp.exception.*;
import kz.ipcorp.model.DTO.SignInRequestDTO;
import kz.ipcorp.model.DTO.SignUpRequestDTO;
import kz.ipcorp.model.DTO.TokenResponseDTO;
import kz.ipcorp.model.DTO.UserViewDTO;
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
    private final BranchService branchService;

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

        if (signUpRequestDTO.getPassword() == null || signUpRequestDTO.getPassword().isEmpty()){
            throw new UserInputException("Password should not be empty");
        }
        if (userRepository.findByPhoneNumber(signUpRequestDTO.getPhoneNumber()).isPresent()){
            throw new DuplicateEntityException("there is already a user with this phone number");
        }

        User user = new User();
        user.setPhoneNumber(signUpRequestDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        log.info("IN createUser - phoneNumber: {}", signUpRequestDTO.getPhoneNumber());
        verificationService.invalidateVerificationCode(signUpRequestDTO.getPhoneNumber());
    }

    public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        User user = userRepository.findByPhoneNumber(signInRequestDTO.getPhoneNumber())
                .orElseThrow(() -> new NotFoundException(
                        String.format("user with phoneNumber %s not found", signInRequestDTO.getPhoneNumber())));

        var access = jwtService.generateToken(user);
        var refresh = jwtService.generateRefreshToken(new HashMap<>(), user);
        if(!passwordEncoder.matches(signInRequestDTO.getPassword(), user.getPassword())){
            throw new AuthenticationException("phoneNumber or password incorrect");
        }
        TokenResponseDTO tokens = new TokenResponseDTO();
        tokens.setAccessToken(access);
        tokens.setRefreshToken(refresh);
        log.info("IN signIn - phoneNumber: {}", signInRequestDTO.getPhoneNumber());
        return tokens;
    }

    public TokenResponseDTO accessToken(String refreshToken) {
        UUID userId = UUID.fromString(jwtService.extractID(refreshToken));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("user with userId %s not found", userId)));
        if (!jwtService.isTokenExpired(refreshToken)) {
            var access = jwtService.generateToken(user);
            log.info("IN accessToken - get access token with refresh token");
            TokenResponseDTO tokens = new TokenResponseDTO();
            tokens.setAccessToken(access);
            tokens.setRefreshToken(refreshToken);
            return tokens;
        }
        log.info("IN accessToken - can't get tokens, refresh token is valid");
        return null;
    }
    @Transactional
    public void resetPassword(SignUpRequestDTO signUpRequestDTO){
        String newPassword = signUpRequestDTO.getPassword();
        String verificationCode = signUpRequestDTO.getVerificationCode();
        String phoneNumber = signUpRequestDTO.getPhoneNumber();

        if(!verificationService.isVerificationCodeValid(phoneNumber, verificationCode)){
            throw new AuthenticationException("sms verification code incorrect");
        }

        if (newPassword == null || newPassword.isEmpty()){
            throw new UserInputException("Password should not be empty");
        }

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException(
                        String.format("user with phoneNumber %s not found", phoneNumber)));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        verificationService.invalidateVerificationCode(phoneNumber);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("user with id %s not found", userId))
        );
        userRepository.delete(user);
    }

    @Transactional
    public void updateBranch(UUID branchId, UUID uuid) {
        branchService.updateBranch(branchId, uuid);
    }

    public UserViewDTO getUserInfo(String language, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        return new UserViewDTO(user, language);
    }
}
