package com.eatza.shared.config.kafka.producer.sharedProducers;

import com.eatza.shared.dto.EmailDto;
import com.eatza.shared.utils.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationProducer {

    private final KafkaTemplate<String, EmailDto> emailKafkaTemplate;

    public ListenableFuture<SendResult<String, EmailDto>> send(EmailDto emailDto) {
        return emailKafkaTemplate.send(Topics.EMAIL_NOTIFICATIOn, emailDto.getUserEmail(), emailDto);
    }
}
