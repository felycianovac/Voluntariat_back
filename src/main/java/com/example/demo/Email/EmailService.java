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

            String confirmationLink = "URL?token=" + token; //replace here
//            msg.setSubject("Verification Email");
            msg.setSubject("Confirmare Email - Voluntariat");
            String htmlContent =
                    "<!DOCTYPE html>" +
                            "<html lang='ro'>" +
                            "<head>" +
                            "  <meta charset='UTF-8' />" +
                            "  <meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
                            "  <title>Confirmare Email - Voluntariat</title>" +
                            "  <style>" +
                            "    body {" +
                            "      font-family: Arial, sans-serif;" +
                            "      line-height: 1.6;" +
                            "      color: #3d3d3d;" +
                            "      max-width: 600px;" +
                            "      margin: 0 auto;" +
                            "      text-align: justify;" +
                            "      padding: 20px;" +
                            "    }" +
                            "    .logo {" +
                            "      text-align: center;" +
                            "      margin-bottom: 20px;" +
                            "    }" +
                            "    .logo img {" +
                            "      max-width: 200px;" +
                            "      height: auto;" +
                            "    }" +
                            "    .content {" +
                            "      background-color: #f5f5f5;" +
                            "      border-radius: 5px;" +
                            "      padding: 20px;" +
                            "    }" +
                            "    .button {" +
                            "      display: inline-block;" +
                            "      background-color: #be99ff;" +
                            "      color: white;" +
                            "      padding: 10px 20px;" +
                            "      text-decoration: none;" +
                            "      border-radius: 15px;" +
                            "    }" +
                            "  </style>" +
                            "</head>" +
                            "<body>" +
                            "  <div class='logo'>" +
                            "    <img src='https://voluntar.md/img/logo.png' alt='Voluntariat Logo' />" +
                            "  </div>" +
                            "  <div class='content'>" +
                            "    <h1>Bine ai venit la Voluntariat!</h1>" +
                            "    <p>Dragă utilizator,</p>" +
                            "    <p>" +
                            "      Îți mulțumim pentru înregistrarea pe platforma noastră de voluntariat." +
                            "      Suntem încântați să te avem alături de noi în această călătorie de a" +
                            "      face lumea un loc mai bun!" +
                            "    </p>" +
                            "    <p>" +
                            "      Pentru a finaliza procesul de înregistrare și a-ți activa contul, te" +
                            "      rugăm să confirmi adresa de email făcând clic pe butonul de mai jos:" +
                            "    </p>" +
                            "    <p style='text-align: center'>" +
                            "      <a href='" + confirmationLink + "' class='button'>Confirmă adresa de email</a>" +
                            "    </p>" +
                            "    <p>" +
                            "      Dacă nu poți face clic pe buton, copiază și lipește următorul link în" +
                            "      browser-ul tău:" +
                            "    </p>" +
                            "    <p>" + confirmationLink + "</p>" +
                            "    <p>" +
                            "      Dacă nu ai creat un cont pe Voluntariat, te rugăm să ignori acest email." +
                            "    </p>" +
                            "    <p>Cu stimă,<br />Echipa Voluntariat</p>" +
                            "  </div>" +
                            "</body>" +
                            "</html>";
            helper.setText(htmlContent, true);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    public void sendOtp(String email, String otp) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setFrom("epetitiimd@gmail.com");
            helper.setTo(email);
            msg.setSubject("Verificare Email - OTP");
            String htmlContent =
                    "<!DOCTYPE html>" +
                            "<html lang='ro'>" +
                            "<head>" +
                            "  <meta charset='UTF-8' />" +
                            "  <meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
                            "  <title>Verificare Email - OTP</title>" +
                            "  <style>" +
                            "    body {" +
                            "      font-family: Arial, sans-serif;" +
                            "      line-height: 1.6;" +
                            "      color: #3d3d3d;" +
                            "      max-width: 600px;" +
                            "      margin: 0 auto;" +
                            "      text-align: justify;" +
                            "      padding: 20px;" +
                            "    }" +
                            "    .logo {" +
                            "      text-align: center;" +
                            "      margin-bottom: 20px;" +
                            "    }" +
                            "    .logo img {" +
                            "      max-width: 200px;" +
                            "      height: auto;" +
                            "    }" +
                            "    .content {" +
                            "      background-color: #f5f5f5;" +
                            "      border-radius: 5px;" +
                            "      padding: 20px;" +
                            "    }" +
                            "    .otp {" +
                            "      font-size: 20px;" +
                            "      font-weight: bold;" +
                            "      text-align: center;" +
                            "      margin: 20px 0;" +
                            "      color: #ff7354;" +
                            "    }" +
                            "    .footer {" +
                            "      margin-top: 20px;" +
                            "      font-size: 0.9em;" +
                            "      color: #7a7a7a;" +
                            "    }" +
                            "  </style>" +
                            "</head>" +
                            "<body>" +
                            "  <div class='logo'>" +
                            "    <img src='https://voluntar.md/img/logo.png' alt='Voluntariat Logo' />" +
                            "  </div>" +
                            "  <div class='content'>" +
                            "    <h1>Verificare Email</h1>" +
                            "    <p>Dragă utilizator,</p>" +
                            "    <p>" +
                            "      Îți mulțumim că te-ai înregistrat la noi. Pentru a-ți verifica adresa de email," +
                            "      te rugăm să introduci următorul cod OTP (One Time Password):" +
                            "    </p>" +
                            "    <div class='otp'>" + otp + "</div>" +
                            "    <p>" +
                            "      Acest OTP este valabil pentru <strong>5 minute</strong> de la primirea acestui email." +
                            "    </p>" +
                            "    <p>Dacă nu ai solicitat această verificare, te rugăm să ignori acest email.</p>" +
                            "    <p>Cu stimă,<br />Echipa Voluntariat</p>" +
                            "  </div>" +
                            "  <div class='footer'>" +
                            "    <p>" +
                            "      Te rugăm să nu răspunzi la acest email. Pentru întrebări, contactează echipa noastră de suport." +
                            "    </p>" +
                            "  </div>" +
                            "</body>" +
                            "</html>";
            helper.setText(htmlContent, true);

            javaMailSender.send(msg);
        } catch (MessagingException e) {
            System.out.println("Error sending OTP email: " + e.getMessage());
        }
    }

}