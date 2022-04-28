package com.eatza.shared.config.redis;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

@Slf4j
public class BearerAuthFeignConfig<T extends CrudRepository<RedisJwtStore, String>> implements RequestInterceptor {

    T redisJwtRepository;

    public BearerAuthFeignConfig(T redisJwtRepository) {
        this.redisJwtRepository = redisJwtRepository;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("adding auth token");
        var user = new ArrayList<RedisJwtStore>();
        redisJwtRepository.findAll().forEach(user::add);
        if (user.size() > 0 && user.get(0).getJwtValue() != null) {
            requestTemplate.header("Authorization", "Bearer " + user.get(0).getJwtValue());
        } else {
            log.error("Unable to add Authoriation header to Feign requestTemplate");
        }
    }
}