package com.eatza.order.messaging.producer;

import com.eatza.shared.dto.orders.OrderRequestDto;
import com.eatza.shared.utils.Topics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;


@Service
@RequiredArgsConstructor
public class OrderInitiatedProducer {

    private final KafkaTemplate<String, OrderRequestDto> orderRequestDtoKafkaTemplate;

    public ListenableFuture<SendResult<String, OrderRequestDto>> send(OrderRequestDto orderRequestDto) {
        return orderRequestDtoKafkaTemplate.send(Topics.ORDER_INITIATED, orderRequestDto.getRestaurantId().toString(), orderRequestDto);
    }
}
