package com.eatza.customer.domain.service;

import com.eatza.shared.config.redis.RedisJwtStore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("redisRepo")
public interface RedisRepository extends CrudRepository<RedisJwtStore, String> {
}