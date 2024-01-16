package kz.ipcorp.service;


import kz.ipcorp.model.DTO.SellerConfirmDTO;
import kz.ipcorp.model.entity.Seller;
import kz.ipcorp.model.entity.Verification;
import kz.ipcorp.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final JavaMailSender mailSender;
    @Value("{$spring.mail.username}")
    private String fromMail;

    public void registerCode(Seller seller) {
        Verification verification = new Verification();
        verification.setSeller(seller);
        verification.setCreatedAt(now()); //30 MIN LIMIT
        verification.setCode(generateCode());
        verificationRepository.save(verification);
        //TODO: mail service send sms
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject("confirm you account");
        simpleMailMessage.setText("короче мына кодты жаз: " + verification.getCode());
        simpleMailMessage.setTo(seller.getEmail());
        mailSender
                .send(simpleMailMessage);
        //subject "confirm you account"
        //text короче мына кодты жаз: verification.getCode()

    }

    @Transactional
    public void checkCode(Seller seller, SellerConfirmDTO sellerConfirmDTO) {
        Verification verification = verificationRepository.
                findBySeller(seller).
                orElseThrow(() -> new IllegalArgumentException("code not found"));
        System.out.println(verification);
        Duration duration = Duration.between(verification.getCreatedAt(), now());
        if (!sellerConfirmDTO.getCode().equals(verification.getCode()) && duration.toMinutes() >= 30) {
            throw new IllegalArgumentException("code is incorrect");
        }

        verification.setIsConfirmed(true);
        verificationRepository.save(verification);
    }

    private Integer generateCode() {
        int min = 100_000; //xxx xxx
        int max = 999_999; // xxx xxx
        return (int)Math.floor(Math.random() * (max - min + 1) + min);
    }
}
