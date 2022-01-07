package com.redis.jedis.pool;

import java.util.function.Consumer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class App {


    static class RedisUtils {

        private RedisUtils() {}
        private static final JedisPool jedisPool = new JedisPool();

        public static void execute(Consumer<Jedis> consumer) {
            Jedis jedis = jedisPool.getResource();
            try {
                consumer.accept(jedis);
            } catch (JedisConnectionException e) {
                // 连接异常，重试一次
                consumer.accept(jedis);
            } finally {
                jedis.close();
            }
        }
    }

    static class Holder<T> {

        public Holder(T data) {
            this.data = data;
        }

        T data;
    }

    public static void main(String[] args) {
        Holder<Long> count = new Holder<>(0L);
        RedisUtils.execute(jedis -> {
            count.data++;
            // 业务逻辑...
            jedis.set("hello", "world");
            // 业务逻辑...
            jedis.close();
        });
    }

}
