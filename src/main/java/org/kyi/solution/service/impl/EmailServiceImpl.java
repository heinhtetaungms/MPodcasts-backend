package org.kyi.solution.service.impl;

import org.kyi.solution.service.EmailService;
import org.kyi.solution.constant.EmailConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendNewPasswordEmail(String firstName, String password, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(email);
        message.setSubject(EmailConstant.EMAIL_SUBJECT);
        String formattedText = """
                Dear %s,
                                
                    We wanted to inform you that your password for your account has been successfully changed.
                    New Password was : %s
                """;
        message.setText(String.format(formattedText, firstName, password));
        mailSender.send(message);
    }
}
