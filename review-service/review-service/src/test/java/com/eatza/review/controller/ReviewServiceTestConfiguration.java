package com.eatza.review.controller;

import com.eatza.review.service.ReviewService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class ReviewServiceTestConfiguration {
    @Bean
    @Primary
    public ReviewService reviewService() {
        return Mockito.mock(ReviewService.class);
    }
}
