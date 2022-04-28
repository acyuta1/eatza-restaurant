package com.eatza.shared.config.kafka.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class LongProducerConfig {
    @Bean
    public ProducerFactory<Long, Long> longProducerFactory(KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildProducerProperties();

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        return new DefaultKafkaProducerFactory<>(props,
                new LongSerializer(),
                new LongSerializer());
    }

    @Bean
    public KafkaTemplate<Long, Long> longProducerTemplate(ProducerFactory<Long, Long> longProducerFactory) {
        return new KafkaTemplate<>(longProducerFactory);
    }
}
