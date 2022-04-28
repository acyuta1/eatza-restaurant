package com.eatza.shared.config.kafka.consumer;

import com.eatza.shared.config.kafka.AppKafkaConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.Map;

public abstract class GenericConsumerConfig<ReceiverObject> {

    protected Map<String, Object> initConsumerFactoryProps(KafkaProperties kafkaProperties, Class value) {
        var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringSerializer.class);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, value);

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "eatza-restaurant");

        return props;
    }

    protected ConcurrentKafkaListenerContainerFactory<String, ReceiverObject> initContainerFactory(
            ConsumerFactory<String, ReceiverObject> cf,
            AppKafkaConsumerProperties appKafkaConsumerProperties,
            MessageConverter messageConverter
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ReceiverObject>();
        factory.setConsumerFactory(cf);
        factory.setConcurrency(appKafkaConsumerProperties.getConcurrency());
        factory.setMessageConverter(messageConverter);
        factory.setAutoStartup(true);

        return factory;
    }
}
