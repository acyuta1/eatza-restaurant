package com.eatza.review.service;

import com.eatza.review.mapper.ReviewMapper;
import com.eatza.review.messaging.producer.ReviewReceivedProducer;
import com.eatza.review.model.Rating;
import com.eatza.shared.dto.restaurants.ReviewReceivedDto;
import com.eatza.shared.dto.reviews.ReviewDto;
import com.eatza.shared.feign.customers.CustomerClient;
import com.eatza.shared.feign.orders.OrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final CustomerClient customerClient;

    private final OrderClient orderClient;

    private final ReviewMapper reviewMapper;

    private final ReviewReceivedProducer reviewReceivedProducer;

    @Override
    @Transactional
    public ReviewDto postReview(ReviewDto reviewDto) {

        // check if order exists
        var order = orderClient.getOrderById(reviewDto.getOrderId()).getBody();

        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order.does.not.exist");
        }

        var rating = Rating.valueOf(reviewDto.getRating());

        if (order.getStatus().equals("INITIATED"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot post review for a new order");

        var customer = customerClient.getCustomer(reviewDto.getCustomerId());

        if (!order.getCustomerId().equals(customer.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "customer.id.does.not.match");

        var reviewReceivedDto = new ReviewReceivedDto()
                .setRating(rating.getRatingValue())
                .setRestaurantId(order.getRestaurantId())
                .setUserEmail(customer.getEmail());

        reviewReceivedProducer.send(reviewReceivedDto);

        // proceed.
        return reviewMapper.toDto(reviewRepository.save(reviewMapper.fromDto(reviewDto)));
    }
}
