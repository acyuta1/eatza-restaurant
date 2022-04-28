package com.eatza.shared.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.kafka.consumer")
@Getter
@Setter
public class AppKafkaConsumerProperties {

    private int concurrency = 1;
}
