package com.eatza.shared.config.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("JwtDetails")
public class RedisJwtStore {

    private String id;

    private String jwtValue;

    private String loggedInUser;
}
