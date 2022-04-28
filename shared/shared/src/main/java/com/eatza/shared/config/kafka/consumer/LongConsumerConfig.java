package com.eatza.shared.config.kafka.consumer;

import com.eatza.shared.config.kafka.AppKafkaConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
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

@Configuration
@EnableKafka
public class LongConsumerConfig {

    @Bean
    public ConsumerFactory<Long, Long> longConsumerFactory(KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildConsumerProperties();

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, LongDeserializer.class);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, LongDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "eatza-restaurant");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Long> kafkaLongListenerContainerFactory(
            @Qualifier("longConsumerFactory") ConsumerFactory<Long, Long> longConsumerFactory,
            AppKafkaConsumerProperties appKafkaConsumerProperties,
            MessageConverter messageConverter
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<Long, Long>();
        factory.setConsumerFactory(longConsumerFactory);
        factory.setConcurrency(appKafkaConsumerProperties.getConcurrency());
        factory.setMessageConverter(messageConverter);
        factory.setAutoStartup(true);
        return factory;
    }
}
