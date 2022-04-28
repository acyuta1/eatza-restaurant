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
public class OrderDeliverConfirmConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = {Topics.ORDER_DELIVERED}, containerFactory = "kafkaLongListenerContainerFactory")
    public void orderDeliveredConsumer(@Payload(required = false) Long orderId, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        if (orderId == null) {
            log.error("Received null in payload");
            throw new RuntimeException("Did not receive proper payload");
        }

        var message = "Your order was successfully delivered. Please leave a review for the restaurant";
        // process
        orderService.updateOrder(orderId,
                message,
                OrderStatus.DELIVERED,
                String.format("Your order %s was delivered", orderId));
    }
}
