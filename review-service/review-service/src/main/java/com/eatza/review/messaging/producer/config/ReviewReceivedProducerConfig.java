package com.eatza.review.messaging.producer.config;

import com.eatza.shared.dto.orders.OrderDeclineDto;
import com.eatza.shared.dto.restaurants.ReviewReceivedDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class ReviewReceivedProducerConfig {

    @Bean
    public ProducerFactory<String, ReviewReceivedDto> reviewReceivedProducerFactory(KafkaProperties kafkaProperties, ObjectMapper objectMapper) {
        var props = kafkaProperties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(props,
                new StringSerializer(),
                new JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, ReviewReceivedDto> reviewReceivedKafkaTemplate(ProducerFactory<String, ReviewReceivedDto> reviewReceivedProducerFactory) {
        return new KafkaTemplate<>(reviewReceivedProducerFactory);
    }
}
