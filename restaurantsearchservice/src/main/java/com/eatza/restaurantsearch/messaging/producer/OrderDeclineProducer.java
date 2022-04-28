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
public class OrderDeclineProducer {

    private final KafkaTemplate<String, OrderDeclineDto> orderDeclineDtoKafkaTemplate;

    public ListenableFuture<SendResult<String, OrderDeclineDto>> send(OrderDeclineDto orderDeclineDto) {
        return orderDeclineDtoKafkaTemplate.send(Topics.ORDER_DECLINED, orderDeclineDto.getOrderId().toString(),orderDeclineDto);
    }
}
