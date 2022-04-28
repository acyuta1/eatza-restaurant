package com.eatza.review.controller;

import com.eatza.review.service.ReviewService;
import com.eatza.shared.dto.reviews.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ReviewDto postReview(@RequestBody @Valid ReviewDto reviewDto) {
        return reviewService.postReview(reviewDto);
    }
}
