package com.eatza.shared.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.kafka.topic")
@Getter
@Setter
public class AppKafkaTopicProperties {

    private Map<String, Short> replicationFactor;
    private Map<String, Integer> partitions;
}
