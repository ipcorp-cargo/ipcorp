package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.SignInRequestDTO;
import kz.ipcorp.model.DTO.SignUpRequestDTO;
import kz.ipcorp.model.DTO.TokenResponseDTO;
import kz.ipcorp.model.DTO.UserViewDTO;
import kz.ipcorp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final Logger log = LogManager.getLogger(AuthController.class);

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        authenticationService.createUser(signUpRequestDTO);
        log.info("IN signUp - phoneNumber: {}", signUpRequestDTO.getPhoneNumber());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/info")
    public ResponseEntity<UserViewDTO> getUserInfo(@CookieValue(name = "Accept-Language", defaultValue = "ru") String language,
                                                   Principal principal) {
        return ResponseEntity.ok(authenticationService.getUserInfo(language, UUID.fromString(principal.getName())));
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenResponseDTO> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
        log.info("IN signIn - phoneNumber: {}, password: ***", signInRequestDTO.getPhoneNumber());
        return ResponseEntity.ok(authenticationService.signIn(signInRequestDTO));
    }



    @PostMapping("/access-token")
    public ResponseEntity<TokenResponseDTO> accessToken(@CookieValue(name = "refresh-token") String refreshToken) {
        log.info("IN accessToken - get access token with refresh token");
        return ResponseEntity.ok(authenticationService.accessToken(refreshToken));
    }

    @PostMapping("/password")
    public ResponseEntity<Void> resetPassword(@RequestBody SignUpRequestDTO signUpRequestDTO){
        authenticationService.resetPassword(signUpRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping()
    public ResponseEntity<HttpStatus> deleteUser(Principal principal) {
        log.info("delete with id {}", UUID.fromString(principal.getName()));
        authenticationService.deleteUser(UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @PostMapping("/branch/{branchId}")
    public ResponseEntity<Void> addBranch(@PathVariable("branchId") UUID branchId,
                                          Principal principal){
        authenticationService.updateBranch(branchId, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
