package com.eatza.restaurantsearch.messaging.consumer.config;

import com.eatza.shared.config.kafka.AppKafkaConsumerProperties;
import com.eatza.shared.config.kafka.consumer.GenericConsumerConfig;
import com.eatza.shared.dto.orders.OrderRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
@Slf4j
public class OrderInitiatedConsumerConfig extends GenericConsumerConfig<OrderRequestDto> {

    @Bean
    public ConsumerFactory<String, OrderRequestDto> orderInitiatedConsumerFactory(KafkaProperties kafkaProperties, ObjectMapper objectMapper) {
        var props = super.initConsumerFactoryProps(kafkaProperties, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props,
                new ErrorHandlingDeserializer<>(new StringDeserializer()),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(OrderRequestDto.class, objectMapper)));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderRequestDto> orderInitatedListenerContainerFactory(
            @Qualifier("orderInitiatedConsumerFactory") ConsumerFactory<String, OrderRequestDto> orderInitatedConsumerFactory,
            AppKafkaConsumerProperties appKafkaConsumerProperties,
            MessageConverter messageConverter
    ) {
        return super.initContainerFactory(orderInitatedConsumerFactory, appKafkaConsumerProperties, messageConverter);
    }
}
