package com.redis.queue;

import redis.clients.jedis.Jedis;

public class Publisher {

    private Jedis jedis;

    public Publisher(Jedis jedis) {
        this.jedis = jedis;
    }

    public void pub(String key, String ...data) {
        this.jedis.rpush(key, data);
    }

}
