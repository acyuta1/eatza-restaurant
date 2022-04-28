package com.eatza.review.messaging.producer;

import com.eatza.shared.dto.restaurants.ReviewReceivedDto;
import com.eatza.shared.utils.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewReceivedProducer {

    private final KafkaTemplate<String, ReviewReceivedDto> reviewReceivedKafkaTemplate;

    public ListenableFuture<SendResult<String, ReviewReceivedDto>> send(ReviewReceivedDto reviewReceivedDto) {
        return reviewReceivedKafkaTemplate.send(Topics.REVIEW_RECEIVED, reviewReceivedDto.getRestaurantId().toString(), reviewReceivedDto);
    }
}
