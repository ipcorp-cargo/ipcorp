package kz.ipcorp.service;

import kz.ipcorp.exception.DuplicateEntityException;
import kz.ipcorp.exception.SMSException;
import kz.ipcorp.model.entity.Verification;
import kz.ipcorp.model.enumuration.SMSRequestType;
import kz.ipcorp.model.util.MobizonSMSSender;
import kz.ipcorp.repository.UserRepository;
import kz.ipcorp.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SMSVerificationService {
    public static final int SMS_REQUEST_LIMIT = 10;
    public static final int LIMIT_RELIEVE_TIME = 24; //unit: hour
    public static final int EXPIRED_TIME = 30; //unit: minute

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final MobizonSMSSender smsSender;
    private final Logger log = LogManager.getLogger(SMSVerificationService.class);

    public void requestSMS(String phoneNumber, SMSRequestType type) {
        if (type != SMSRequestType.FORGOT_PASSWORD){
            if(userRepository.findByPhoneNumber(phoneNumber).isPresent()){
                throw new DuplicateEntityException(String.format("user not found with phoneNumber %s", phoneNumber));
            }
        }

        Verification verification = verificationRepository.findById(phoneNumber)
                .orElseGet(() -> new Verification(phoneNumber, null));

        log.info("verification phoneNumber: {}, status: {}", verification.getPhoneNumber(), verification.isValid());
        if(verification.getCount() >= SMS_REQUEST_LIMIT){
            var now = getTime();
            var nextRequestTime = verification.getCreationDate().plusHours(LIMIT_RELIEVE_TIME);
            if(now.isBefore(nextRequestTime)){
                throw new SMSException("sms request max");
            } else {
                verification.setCount(0);
            }
        }

        verification.setCode(generateVerificationCode());
        verification.setCount(verification.getCount() + 1);
        verification.setCreationDate(getTime());
        verification.setValid(true);
        smsSender.sendSMS(verification.getPhoneNumber(), verification.getCode());
        verificationRepository.save(verification);
    }

    public boolean isVerificationCodeValid(String phoneNumber, String verificationCode){
        Optional<Verification> optionalVerification = verificationRepository.findById(phoneNumber);
        if(optionalVerification.isEmpty()){
            return false;
        }

        Verification verification = optionalVerification.get();

        log.info("phoneNumber: {}, isValid: {}", verification.getPhoneNumber(), verification.isValid());
        var now = getTime();
        if (now.isAfter(verification.getCreationDate().plusMinutes(EXPIRED_TIME))){
            verification.setValid(false);
        }
        return verification.isValid() && verification.getCode().equals(verificationCode);
    }

    public void invalidateVerificationCode(String phoneNumber){
        Optional<Verification> optionalVerification = verificationRepository.findById(phoneNumber);
        if(optionalVerification.isEmpty()){
            return;
        }

        Verification verification = optionalVerification.get();
        verification.setValid(false);
        verificationRepository.save(verification);
    }

    public LocalDateTime getTime(){
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    private String generateVerificationCode(){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i< Verification.VERIFICATION_CODE_LENGTH; i++){
            builder.append((int)(Math.random()*10));
        }
        return builder.toString();
    }
}
