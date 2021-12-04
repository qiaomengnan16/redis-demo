package com.redis.delayed.queue;

import redis.clients.jedis.Jedis;

public class Publisher {

    private Jedis jedis;

    public Publisher(Jedis jedis) {
        this.jedis = jedis;
    }

    public void pub(String key, String data, int time) {
        // 发布延迟 time 时间的消息
        this.jedis.zadd(key, System.currentTimeMillis() + (time * 1000), data);
    }

}
