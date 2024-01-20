package kz.ipcorp.controller;


import kz.ipcorp.model.DTO.*;
import kz.ipcorp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //TODO: sign in method token accessToken, refreshToken
    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@RequestBody SellerCreateDTO sellerCreateDTO) {
        authService.registerSeller(sellerCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/confirm")
    public ResponseEntity<HttpStatus> confirm(@RequestBody SellerConfirmDTO sellerConfirmDTO) {
        authService.confirmSeller(sellerConfirmDTO);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenResponseDTO> signin(@RequestBody SignInRequestDTO signInRequestDTO){
        return new ResponseEntity<>(authService.signIn(signInRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/access-token")
    public ResponseEntity<TokenResponseDTO> accessToken(@CookieValue String refreshToken) {
        return ResponseEntity.ok(authService.accessToken(refreshToken));
    }
}
