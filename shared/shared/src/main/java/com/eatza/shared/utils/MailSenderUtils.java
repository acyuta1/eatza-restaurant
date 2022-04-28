package com.eatza.shared.utils;

import com.eatza.shared.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSenderUtils {

    private final JavaMailSender javaMailSender;

    /**
     * To send a mail when provided with the target email, the subject and the message.
     * @param emailDto the email dto consisting of target email, subject and message
     */
    public void sendMail(EmailDto emailDto) {

        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(emailDto.getUserEmail());

        msg.setFrom("notification@eatza.com");

        msg.setSubject(emailDto.getSubject());

        msg.setText(emailDto.getMessage());

        javaMailSender.send(msg);

    }
}

