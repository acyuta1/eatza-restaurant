package com.eatza.restaurantsearch.messaging.producer;

import com.eatza.shared.dto.orders.OrderDeclineDto;
import com.eatza.shared.utils.Topics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@RequiredArgsConstructor
public class OrderDeliveryConfirmProducer {

    private final KafkaTemplate<Long, Long> longProducerTemplate;

    public ListenableFuture<SendResult<Long, Long>> send(Long orderId) {
        return longProducerTemplate.send(Topics.ORDER_DELIVERED, orderId);
    }
}
