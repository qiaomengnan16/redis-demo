package com.redis.cell;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class SimpleRateLimiter {

    /**
     * isActionAllowed
     *
     * @param userId 用户id
     * @param actionKey 操作的key
     * @param period 时间窗口
     * @param maxCount 最大限制
     * @return
     */
    public static boolean isActionAllowed(String userId, String actionKey, int period, int maxCount) {
        String key = String.format("hist:%s:%s", userId, actionKey);
        // 毫秒时间戳
        long nowTs = System.currentTimeMillis();
        // 创建redis连接
        Jedis jedis = JedisBuilder.buildJedis();
        Pipeline pipe = jedis.pipelined();
        pipe.multi();
        // 在zset中添加用户的行为
        pipe.zadd(key, nowTs, nowTs + "");
        // 删除时间窗口外的数据
        pipe.zremrangeByScore(key, 0, nowTs - period * 1000);
        // 获取时间窗口内的总数
        Response<Long> res = pipe.zcard(key);
        // 对key设置有效期
        pipe.expire(key, period + 1);
        pipe.exec();
        pipe.close();
        // 窗口内的总数是否小于等于最大限制
        return res.get() <= maxCount;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(isActionAllowed("mn", "reply", 60, 5));
        }
    }

}
