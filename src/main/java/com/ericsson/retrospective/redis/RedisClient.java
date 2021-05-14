package com.ericsson.retrospective.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisClient {
    private RedissonClient redissonClient;

    public RedisClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        this.redissonClient = Redisson.create(config);
    }


}
