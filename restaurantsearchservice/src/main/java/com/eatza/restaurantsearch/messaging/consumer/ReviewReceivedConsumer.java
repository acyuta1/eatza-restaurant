package com.eatza.restaurantsearch.messaging.consumer;

import com.eatza.restaurantsearch.service.restaurantservice.RestaurantService;
import com.eatza.shared.dto.restaurants.ReviewReceivedDto;
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
public class ReviewReceivedConsumer {

    private final RestaurantService restaurantService;

    @KafkaListener(topicPattern = Topics.REVIEW_RECEIVED, containerFactory = "reviewReceivedKafkaListenerContainerFactory")
    public void reviewReceivedConsumer(@Payload(required = false) ReviewReceivedDto reviewReceivedDto, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        restaurantService.processReviewReceived(reviewReceivedDto);
    }
}
