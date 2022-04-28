package com.eatza.order.messaging.consumer;

import com.eatza.order.model.OrderStatus;
import com.eatza.order.service.orderservice.OrderService;
import com.eatza.shared.dto.orders.OrderDeclineDto;
import com.eatza.shared.utils.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDeclineConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = {Topics.ORDER_DECLINED}, containerFactory = "orderDeclinedListenerContainerFactory")
    public void orderDeclineConsumer(@Payload(required = false) OrderDeclineDto orderDeclineDto, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        if (orderDeclineDto == null) {
            log.error("Received null in payload");
            throw new RuntimeException("Did not receive proper payload");
        }

        // process
        orderService.updateOrder(orderDeclineDto.getOrderId(),
                        orderDeclineDto.getReason(),
                        OrderStatus.DENIED,
                        String.format("Your order %s is declined.", orderDeclineDto.getOrderId()));
    }
}
