package com.redis.delayed.queue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

    // https://www.coder.work/article/260919
    private static final String lua_script = "local key = KEYS[1]\n"
            + "local value = ARGV[1]\n"
            + "local result = redis.call(\"ZRANGEBYSCORE\", key, 0, tonumber(value), 'limit' , 0, 1)\n"
            + "if result ~= false and #result ~= 0 then\n"
            + " redis.call('ZREM', key, result[1])\n"
            + " return result[1]\n"
            + "end";

    public String subLua(String key) {
        // 取时间范围大于等于0，小于等于当前时间的数据中的第一条
        Object res = this.jedis.eval(lua_script, Arrays.asList(key), Arrays.asList(System.currentTimeMillis()+""));
        return res == null ? null : res.toString();
    }

}
