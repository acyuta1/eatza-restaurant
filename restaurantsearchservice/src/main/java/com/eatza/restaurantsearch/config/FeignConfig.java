package com.eatza.restaurantsearch.config;

import com.eatza.restaurantsearch.repository.RedisRepository;
import com.eatza.shared.config.redis.BearerAuthFeignConfig;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final RedisRepository repository;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new BearerAuthFeignConfig<>(repository);
    }
}
