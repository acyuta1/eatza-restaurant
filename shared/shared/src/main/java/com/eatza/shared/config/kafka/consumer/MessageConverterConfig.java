package com.eatza.shared.config.kafka.consumer;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaderMapper;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.StringUtils;

@Configuration
public class MessageConverterConfig {

    @Bean
    public KafkaHeaderMapper kafkaHeaderMapper(KafkaProperties kafkaProperties) {
        var kafkaHeaderMapper = new DefaultKafkaHeaderMapper();

        var props = kafkaProperties.buildConsumerProperties();
        if (props.containsKey(JsonDeserializer.TRUSTED_PACKAGES) && props.get(JsonDeserializer.TRUSTED_PACKAGES) != null) {
            var trustedPackages = StringUtils.commaDelimitedListToStringArray((String) props.get(JsonDeserializer.TRUSTED_PACKAGES));
            kafkaHeaderMapper.addTrustedPackages(trustedPackages);
        }
        return kafkaHeaderMapper;
    }

    @Bean
    public MessageConverter messageConverter(KafkaHeaderMapper kafkaHeaderMapper) {
        var messageConverter = new MessagingMessageConverter();
        messageConverter.setHeaderMapper(kafkaHeaderMapper);
        return messageConverter;
    }
}
