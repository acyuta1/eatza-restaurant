package com.eatza.customer.domain.messaging;

import com.eatza.shared.dto.EmailDto;
import com.eatza.shared.dto.orders.OrderDeclineDto;
import com.eatza.shared.utils.MailSenderUtils;
import com.eatza.shared.utils.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationConsumer {

    private final MailSenderUtils mailSenderUtils;

    @KafkaListener(topics = {Topics.EMAIL_NOTIFICATIOn}, containerFactory = "emailNotificationListenerContainerFactory")
    public void emailNotificationConsumer(@Payload(required = false) EmailDto emailDto, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        if (emailDto == null) {
            log.error("Received null in payload");
            throw new RuntimeException("Did not receive proper payload");
        }

        log.info("sending email notification with subject: {}", emailDto.getSubject());
        mailSenderUtils.sendMail(emailDto);
    }
}
