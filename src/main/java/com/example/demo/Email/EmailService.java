package com.example.demo.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private EmailDetails emailDetails;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationToken(String email, String token) {
        try {

            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setFrom("epetitiimd@gmail.com");
            helper.setTo(email);
            msg.setSubject("Verification Email");
            String htmlContent = "<h1>Verification Email</h1>" +
                    "<p>Click the link below to verify your email address.</p>" +
                    "<a href='http://localhost:8080/api/auth/confirm-email?token=" + token + "'>Verify Email</a>";
            helper.setText(htmlContent, true);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

}