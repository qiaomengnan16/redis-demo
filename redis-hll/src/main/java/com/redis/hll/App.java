package com.redis.hll;

import redis.clients.jedis.Jedis;

public class App {


    public static void main(String[] args) {
        Jedis jedis = JedisBuilder.buildJedis();
        for (int i = 0; i < 10000; i++) {
            jedis.set("key" + i, i+"");
        }
        if (1==1) {
            return;
        }
        
        int count = 100000;
        String key = "hello-hll-5";
        for (int i = 0; i < count; i++) {
            jedis.pfadd(key, "user" + i);
        }
        long countRes = jedis.pfcount(key);
        System.out.printf("%d %d", count, countRes);
        jedis.close();
    }

}
