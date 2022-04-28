package com.eatza.order.messaging.producer.config;

import com.eatza.shared.dto.orders.OrderRequestDto;
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
public class OrderInitiatedProducerConfig {

    @Bean
    public ProducerFactory<String, OrderRequestDto> orderRequestDtoProducerFactory(KafkaProperties kafkaProperties, ObjectMapper objectMapper) {
        var props = kafkaProperties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(props,
                new StringSerializer(),
                new JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, OrderRequestDto> orderRequestDtoKafkaTemplate(ProducerFactory<String, OrderRequestDto> orderRequestDtoProducerFactory) {
        return new KafkaTemplate<>(orderRequestDtoProducerFactory);
    }
}
