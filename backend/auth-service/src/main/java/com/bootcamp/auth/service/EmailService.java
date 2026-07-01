package com.bootcamp.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendMail(
            String to,
            String subject,
            String body
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

    }

    public void sendWelcomeMail(
            String email,
            String fullName,
            String temporaryPassword
    ) {

        String body = """
                Hello %s,

                Welcome to Bootcamp LMS.

                Your account has been created successfully.

                Temporary Password: %s

                Please login and change your password immediately.

                Regards,
                Bootcamp Team
                """
                .formatted(fullName, temporaryPassword);

        sendMail(
                email,
                "Welcome to Bootcamp LMS",
                body
        );

    }

    public void sendOtpMail(
            String to,
            String fullName,
            String otp
    ) {

        String body = """
                Hello %s,

                Your OTP for password reset is:

                %s

                This OTP is valid for 5 minutes.

                If you did not request this, please ignore this email.

                Regards,
                Bootcamp LMS Team
                """
                .formatted(fullName, otp);

        sendMail(
                to,
                "Bootcamp LMS - Password Reset OTP",
                body
        );

    }

}