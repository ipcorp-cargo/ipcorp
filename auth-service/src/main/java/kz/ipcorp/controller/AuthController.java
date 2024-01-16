package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.AccessTokenRequestDTO;
import kz.ipcorp.model.DTO.SignInRequestDTO;
import kz.ipcorp.model.DTO.SignUpRequestDTO;
import kz.ipcorp.model.DTO.TokenResponseDTO;
import kz.ipcorp.model.entity.User;
import kz.ipcorp.service.AuthenticationService;
import kz.ipcorp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        return ResponseEntity.ok(authenticationService.createUser(signUpRequestDTO));
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenResponseDTO> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
        return ResponseEntity.ok(authenticationService.signIn(signInRequestDTO));
    }



    @PostMapping("/access-token")
    public ResponseEntity<TokenResponseDTO> accessToken(@RequestBody AccessTokenRequestDTO accessTokenRequestDTO) {
        return ResponseEntity.ok(authenticationService.accessToken(accessTokenRequestDTO));
    }
}
