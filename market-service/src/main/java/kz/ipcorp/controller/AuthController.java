package kz.ipcorp.controller;


import kz.ipcorp.model.DTO.*;
import kz.ipcorp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final Logger log = LogManager.getLogger(AuthController.class);
    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@RequestBody SellerCreateDTO sellerCreateDTO) {
        log.info("IN signup with email: {}", sellerCreateDTO.getEmail());
        authService.createSeller(sellerCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("/signin")
    public ResponseEntity<TokenResponseDTO> signin(@RequestBody SignInRequestDTO signInRequestDTO){
        log.info("IN signin with email: {} {}", signInRequestDTO.getEmail(), signInRequestDTO.getPassword());
        return new ResponseEntity<>(authService.signIn(signInRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/access-token")
    public ResponseEntity<TokenResponseDTO> accessToken(@CookieValue(name = "refreshToken") String refreshToken) {
        log.info("cookie {}", refreshToken);
        return ResponseEntity.ok(authService.accessToken(refreshToken));
    }


    @PostMapping("/password")
    public ResponseEntity<Void> resetPassword(@RequestBody SellerCreateDTO sellerCreateDTO){
        authService.resetPassword(sellerCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
