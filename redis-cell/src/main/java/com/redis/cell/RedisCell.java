package com.redis.cell;

import io.lettuce.core.RedisClient;

public class RedisCell {

    public static void main(String[] args) throws InterruptedException {
        RedisClient client = RedisClient.create("redis://127.0.0.1:6379");
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(client);
        for (int i = 1; i <= 30; i++) {
            boolean success = limiter.isActionAllowed("user1:zan", 15, 30, 60, 1);
            System.out.println("第" + i + "次请求" + (success ? "成功" : "失败"));
        }
    }

}
