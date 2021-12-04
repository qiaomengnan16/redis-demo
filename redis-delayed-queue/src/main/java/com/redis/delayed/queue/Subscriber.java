package com.redis.delayed.queue;

import java.util.Set;
import java.util.stream.Collectors;
import redis.clients.jedis.Jedis;

public class Subscriber {

    private Jedis jedis;

    public Subscriber(Jedis jedis) {
        this.jedis = jedis;
    }

    public String sub(String key) {
        // 取时间范围大于等于0，小于等于当前时间的数据中的第一条
        Set<String> res = this.jedis.zrangeByScore(key, 0, System.currentTimeMillis(), 0, 1);
        String data = res == null || res.isEmpty() ? null : res.iterator().next();
        if (data != null) {
            // zrem 确认自己获取到
            if (jedis.zrem(key, data) > 0) {
                return data;
            }
        }
        return data;
    }

}
