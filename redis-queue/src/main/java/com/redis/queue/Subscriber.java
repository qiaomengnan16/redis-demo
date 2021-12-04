package com.redis.queue;

import java.util.List;
import java.util.stream.Collectors;
import redis.clients.jedis.Jedis;

public class Subscriber {

    private Jedis jedis;

    public Subscriber(Jedis jedis) {
        this.jedis = jedis;
    }

    public String sub(int timeout, String key) {
        List<String> res = this.jedis.blpop(timeout, key);
        return res == null ? null : res.stream().collect(Collectors.joining(","));
    }

}
