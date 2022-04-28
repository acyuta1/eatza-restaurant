package com.eatza.review.abcd;

import com.eatza.review.mapper.ReviewMapper;
import com.eatza.review.mapper.ReviewMapperImpl;
import com.eatza.review.messaging.producer.ReviewReceivedProducer;
import com.eatza.review.messaging.producer.config.ReviewReceivedProducerConfig;
import com.eatza.review.model.Review;
import com.eatza.review.service.ReviewRepository;
import com.eatza.review.service.ReviewService;
import com.eatza.review.service.ReviewServiceImpl;
import com.eatza.shared.config.kafka.producer.sharedProducers.EmailNotificationProducer;
import com.eatza.shared.dto.customers.CustomerDto;
import com.eatza.shared.dto.orders.OrderDto;
import com.eatza.shared.dto.restaurants.ReviewReceivedDto;
import com.eatza.shared.dto.reviews.ReviewDto;
import com.eatza.shared.feign.customers.CustomerClient;
import com.eatza.shared.feign.orders.OrderClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest(classes = {ReviewService.class, ReviewMapperImpl.class})
public class ReviewControllerTest {

    ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        reviewService = new ReviewServiceImpl(
                reviewRepository,
                customerClient,
                orderClient,
                reviewMapper,
                reviewReceivedProducer
        );
    }

    @Mock
    OrderClient orderClient;

    @Mock
    CustomerClient customerClient;

    @Mock
    ReviewReceivedProducer reviewReceivedProducer;

    @Mock
    EmailNotificationProducer emailNotificationProducer;

    @Mock
    ReviewReceivedProducerConfig reviewReceivedProducerConfig;

    @Mock
    ReviewRepository reviewRepository;

    @Autowired
    ReviewMapper reviewMapper;

    @Test
    public void testWhen_reviewPosted() {

        var review = new ReviewDto();
        review.setRating("PERFECT");
        review.setOrderId(1L);
        review.setCustomerId(1L);

        var order = new OrderDto();
        order.setId(1L);
        order.setCustomerId(1L);
        order.setRestaurantId(1L);
        order.setStatus("DELIVERED");
        ResponseEntity<OrderDto> myEntity = new ResponseEntity<OrderDto>(order, HttpStatus.ACCEPTED);
        Mockito.when(orderClient.getOrderById(Mockito.anyLong())).thenReturn(myEntity);

        var customer = new CustomerDto();
        customer.setId(1L);
        Mockito.when(customerClient.getCustomer(Mockito.anyLong())).thenReturn(customer);

        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(reviewMapper.fromDto(review));

        Mockito.when(reviewReceivedProducer.send(any(ReviewReceivedDto.class))).thenReturn(any());

        var result = reviewService.postReview(review);

        Assertions.assertEquals(result.getRating(), "PERFECT");
        Assertions.assertEquals(result.getOrderId(), 1L);
        Assertions.assertEquals(result.getCustomerId(), 1L);
    }
}
