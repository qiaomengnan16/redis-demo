package com.redis.pipeline.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class App {

    public static void main(String[] args) {
        System.out.println("非pipeline: " + noPipeline());
        System.out.println("pipeline: " + pipeline());
    }

    public static long noPipeline() {
        Jedis jedis = JedisBuilder.buildJedis();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 6_000; i++) {
            jedis.set("i" + i, i + "");
        }
        long end = System.currentTimeMillis();
        return (end - begin) / 1000;
    }

    public static long pipeline() {
        Jedis jedis = JedisBuilder.buildJedis();
        Pipeline pipeline = jedis.pipelined();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 6_000; i++) {
            pipeline.set("i" + i, i + "");
        }
        pipeline.sync();
        long end = System.currentTimeMillis();
        return (end - begin) / 1000;
    }


}
