package com.eatza.restaurantsearch.messaging.consumer;

import com.eatza.restaurantsearch.service.restaurantservice.RestaurantService;
import com.eatza.shared.dto.orders.OrderRequestDto;
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
public class OrderInitiatedConsumer {

    private final RestaurantService restaurantService;

    @KafkaListener(topicPattern = Topics.ORDER_INITIATED, containerFactory = "orderInitatedListenerContainerFactory")
    public void receiveOrderInitiatedDetails(@Payload(required = false) OrderRequestDto orderRequestDto, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("read {} {}", orderRequestDto.getCustomerId(), orderRequestDto.getRestaurantId());

        restaurantService.processOrderInit(orderRequestDto);
    }
}
