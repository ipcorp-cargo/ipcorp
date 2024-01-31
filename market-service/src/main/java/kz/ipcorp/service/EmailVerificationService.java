package kz.ipcorp.service;


import kz.ipcorp.exception.DuplicateEntityException;
import kz.ipcorp.exception.SMSException;
import kz.ipcorp.model.entity.Verification;
import kz.ipcorp.model.enumuration.SMSRequestType;
import kz.ipcorp.repository.SellerRepository;
import kz.ipcorp.repository.VerificationRepository;
import kz.ipcorp.util.GmailSMSSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    public static final int SMS_REQUEST_LIMIT = 10;
    public static final int LIMIT_RELIEVE_TIME = 24; //unit: hour
    public static final int EXPIRED_TIME = 30; //unit: minute

    private final VerificationRepository verificationRepository;
    private final SellerRepository sellerRepository;
    private final GmailSMSSender gmailSMSSender;
    private final Logger log = LogManager.getLogger(EmailVerificationService.class);

    @Transactional
    public void requestSMS(String email, SMSRequestType smsType){
        if(smsType != SMSRequestType.FORGOT_PASSWORD){
            if (sellerRepository.findByEmail(email).isPresent()){
                throw new DuplicateEntityException(String.format("seller not found with email %s", email));
            }
        }

        Verification verification = verificationRepository.findById(email)
                .orElseGet(() -> new Verification(email, null));

        if (verification.getCount() >= SMS_REQUEST_LIMIT){
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
        gmailSMSSender.smsSender(verification.getEmail(), verification.getCode());
        verificationRepository.save(verification);
    }

    @Transactional(readOnly = true)
    public boolean isVerificationCodeValid(String email, String verificationCode){
        Optional<Verification> optionalVerification = verificationRepository.findById(email);
        if(optionalVerification.isEmpty()){
            return false;
        }

        Verification verification = optionalVerification.get();

        var now = getTime();
        if(now.isAfter(verification.getCreationDate().plusMinutes(EXPIRED_TIME))){
            verification.setValid(false);
        }

        return verification.isValid() && verification.getCode().equals(verificationCode);
    }

    @Transactional
    public void invalidateVerificationCode(String email){
        Optional<Verification> optionalVerification = verificationRepository.findById(email);

        if (optionalVerification.isEmpty()){
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
