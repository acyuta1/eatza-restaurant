package com.eatza.review.service;

import com.eatza.shared.dto.reviews.ReviewDto;

public interface ReviewService {

    ReviewDto postReview(ReviewDto reviewDto);
}
