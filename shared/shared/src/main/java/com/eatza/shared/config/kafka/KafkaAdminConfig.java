package com.eatza.shared.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

@Configuration
public class KafkaAdminConfig {
    private static final int TOPIC_PARTITION_DEFAULT = 3;
    private static final short TOPIC_REPLICATION_FACTOR_DEFAULT = 1;

    private final AppKafkaTopicProperties appKafkaTopicProperties;

    public KafkaAdminConfig(AppKafkaTopicProperties appKafkaTopicProperties) {
        this.appKafkaTopicProperties = appKafkaTopicProperties;
    }

    @Bean
    public KafkaAdmin admin(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildAdminProperties();
        return new KafkaAdmin(props);
    }

    @Bean
    public NewTopic orderInitiatedTopic() {
        return newTopicByName("eatza-order-initiated");
    }

    @Bean
    public NewTopic orderDeclinedTopic() {
        return newTopicByName("eatza-order-declined");
    }

    @Bean
    public NewTopic orderDeliverConfirmTopic() {
        return newTopicByName("eatza-order-delivered");
    }

    @Bean
    public NewTopic emailTopic() {
        return newTopicByName("eatza-email-notification");
    }

    @Bean
    public NewTopic reviewReceived() {
        return newTopicByName("eatza-review-received");
    }

    private NewTopic newTopicByName(String topicName) {
        var partitions = getPartitionsByTopicName(topicName);
        var replications = getReplicationFactorByTopicName(topicName);
        return new NewTopic(topicName, partitions, replications);
    }

    private short getReplicationFactorByTopicName(String topicName) {
        var topicNameReplicationFactorCountMap = appKafkaTopicProperties.getReplicationFactor();
        if (topicNameReplicationFactorCountMap != null) {
            var rf = topicNameReplicationFactorCountMap.get(topicName);
            if (rf != null)
                return rf;
        }

        return TOPIC_REPLICATION_FACTOR_DEFAULT;
    }

    private int getPartitionsByTopicName(String topicName) {
        var topicNamePartitionCountMap = appKafkaTopicProperties.getPartitions();
        if (topicNamePartitionCountMap != null) {
            var paritionCount = topicNamePartitionCountMap.get(topicName);
            if (paritionCount != null) {
                return paritionCount;
            }
        }
        return TOPIC_PARTITION_DEFAULT;
    }

}
