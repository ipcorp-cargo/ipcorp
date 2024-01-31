package kz.ipcorp.controller;

import kz.ipcorp.model.DTO.SMSRequestDTO;
import kz.ipcorp.model.DTO.SMSVerificationDTO;
import kz.ipcorp.service.SMSVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * TODO: 2 post method
 *
 * 1 method -> SMSRequestDTO(phoneNumber, SMSRequestType(FORGOT_PASSWORD, REGISTER))
 * LOGICASI SMS JIBERY (30 minut limit, max 3 sms)
 *
 *
 * 2 method -> SMSVerificationDTO(phoneNumber, verificationCode(6 symbol))
 * LOGICASI VERIFY JASAU SMS CODDI
 * */
@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SMSController {
    private final SMSVerificationService verificationService;
    @PostMapping
    public ResponseEntity<String> requestNewSMS(@RequestBody SMSRequestDTO smsRequestDTO){
        verificationService.requestSMS(smsRequestDTO.getPhoneNumber(), smsRequestDTO.getSmsRequestType());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/verify")
    public ResponseEntity<Void> verifySMSCode(@RequestBody SMSVerificationDTO smsVerificationDTO){
        if(verificationService.isVerificationCodeValid(
                smsVerificationDTO.getPhoneNumber(),
                smsVerificationDTO.getVerificationCode()
        )){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(431));
        }
    }
}