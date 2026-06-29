package com.bootcamp.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeMail(
            String email,
            String fullName,
            String temporaryPassword
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);

        message.setSubject("Welcome to Bootcamp LMS");

        message.setText("""
                Hello %s,

                Welcome to Bootcamp LMS.

                Your account has been created successfully.

                Temporary Password: %s

                Please login and change your password immediately.

                Regards,
                Bootcamp Team
                """.formatted(fullName, temporaryPassword));

        mailSender.send(message);
    }
}