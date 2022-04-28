package com.eatza.customer.domain.messaging.config;

import com.eatza.shared.config.kafka.AppKafkaConsumerProperties;
import com.eatza.shared.config.kafka.consumer.GenericConsumerConfig;
import com.eatza.shared.dto.EmailDto;
import com.eatza.shared.dto.orders.OrderDeclineDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;


@Configuration
@Slf4j
@EnableKafka
public class EmailNotificationConsumerConfig extends GenericConsumerConfig<EmailDto> {

    @Bean
    public ConsumerFactory<String, EmailDto> emailNotificationConsumerFactory(KafkaProperties kafkaProperties, ObjectMapper objectMapper) {
        var props = super.initConsumerFactoryProps(kafkaProperties, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props,
                new ErrorHandlingDeserializer<>(new StringDeserializer()),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(EmailDto.class, objectMapper)));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailDto> emailNotificationListenerContainerFactory(
            @Qualifier("emailNotificationConsumerFactory") ConsumerFactory<String, EmailDto> emailNotificationConsumerFactory,
            AppKafkaConsumerProperties appKafkaConsumerProperties,
            MessageConverter messageConverter
    ) {
        return super.initContainerFactory(emailNotificationConsumerFactory, appKafkaConsumerProperties, messageConverter);
    }
}
