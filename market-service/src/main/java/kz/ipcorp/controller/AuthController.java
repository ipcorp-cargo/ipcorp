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
        authService.registerSeller(sellerCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/sms")
    public ResponseEntity<HttpStatus> sendSMS(@RequestBody SMSRequestDTO smsRequestDTO) {
        authService.sendSMS(smsRequestDTO);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/confirm")
    public ResponseEntity<HttpStatus> confirm(@RequestBody SellerConfirmDTO sellerConfirmDTO) {
        log.info("IN confirm with email: {}", sellerConfirmDTO.getEmail());
        authService.confirmSeller(sellerConfirmDTO);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenResponseDTO> signin(@RequestBody SignInRequestDTO signInRequestDTO){
        log.info("IN signin with email: {} {}", signInRequestDTO.getEmail(), signInRequestDTO.getPassword());
        return new ResponseEntity<>(authService.signIn(signInRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/access-token")
    public ResponseEntity<TokenResponseDTO> accessToken(@CookieValue(name="refreshToken") String refreshToken) {
        return ResponseEntity.ok(authService.accessToken(refreshToken));
    }
}
