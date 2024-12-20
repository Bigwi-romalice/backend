package com.shopery.vendor.inventory.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;

    public void sendTwoFactorCode(String email, String twoFactorCode) {
        try {
            MimeMessageHelper message = new MimeMessageHelper(mailSender.createMimeMessage());
            message.setTo(email);
            message.setSubject("Your 2FA Code");
            message.setText("Your two-factor authentication code is: " + twoFactorCode);

            mailSender.send(message.getMimeMessage());
        } catch (Exception e) {
            logger.error("An error occurred", e);
        }
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
