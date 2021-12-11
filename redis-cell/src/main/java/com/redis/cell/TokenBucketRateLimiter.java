package com.redis.cell;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.dynamic.RedisCommandFactory;
import java.util.List;

public class TokenBucketRateLimiter {

    private static final String SUCCESS = "0";
    private RedisClient client;
    private StatefulRedisConnection<String, String> connection;
    private IRedisCommand command;

    public TokenBucketRateLimiter(RedisClient client) {
        this.client = client;
        this.connection = client.connect();
        this.command = new RedisCommandFactory(connection).getCommands(IRedisCommand.class);
    }

    public boolean isActionAllowed(String key, long maxBurst, long tokens, long seconds, long apply) {
        List<Object> result = command.throttle(key, maxBurst, tokens, seconds, apply);
        if (result != null && result.size() > 0) {
            return SUCCESS.equals(result.get(0).toString());
        }
        return false;
    }

}
