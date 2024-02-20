package kz.ipcorp.util;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GmailSMSSender {
    private final JavaMailSender mailSender;
    @Value("{$spring.mail.username}")
    private String fromMail;
    private final Logger log = LogManager.getLogger(GmailSMSSender.class);
    public void smsSender(String toGmail, String verificationCode){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject("验证您的账号");
        simpleMailMessage.setText(String.format(
                """
                尊敬的用户：
                                
                您的验证码是：[%s]。请勿将此验证码透露给任何人，包括平台工作人员。如非本人操作，请忽略本短信。
                                
                [VENDOR IPCORP]
                
                """, verificationCode
        ));
        simpleMailMessage.setTo(toGmail);
        log.info("send code to gmail: {}", toGmail);
        mailSender.send(simpleMailMessage);
    }
}
