package kz.ipcorp.controller;


import kz.ipcorp.model.DTO.SMSRequestDTO;
import kz.ipcorp.model.DTO.SMSVerificationDTO;
import kz.ipcorp.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emails")
public class EmailController {
    private final EmailVerificationService verificationService;

    @PostMapping
    public ResponseEntity<String> requestSMS(@RequestBody SMSRequestDTO smsRequestDTO) {
        verificationService.requestSMS(smsRequestDTO.getEmail(), smsRequestDTO.getSmsRequestType());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifySMSCode(@RequestBody SMSVerificationDTO smsVerificationDTO) {
        if (verificationService.isVerificationCodeValid(
                smsVerificationDTO.getEmail(),
                smsVerificationDTO.getVerificationCode()
        )) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(431));
        }
    }
}
