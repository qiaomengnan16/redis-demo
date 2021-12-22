package com.demo.redis.zip;

import redis.clients.jedis.Jedis;

public class App {

//    public static void main(String[] args) {
//        Jedis jedis = JedisBuilder.buildJedis();
//        String key = "hello";
//        jedis.del(key);
//        for (int i = 0; i < 512; i++) {
//            jedis.hset(key, i + "", i + "");
//        }
//        System.out.println(jedis.objectEncoding(key) + " " + jedis.hlen(key));
//        jedis.hset(key, "512", "512");
//        System.out.println(jedis.objectEncoding(key) + " " + jedis.hlen(key));
//    }

    public static void main(String[] args) {
        Jedis jedis = JedisBuilder.buildJedis();
        String key = "hello";
        jedis.del(key);
        String value = "";
        for (int i = 0; i < 64; i++) {
            value += "1";
            jedis.hset(key, value, value);
        }
        System.out.println(jedis.objectEncoding(key) + " " + value + " " + value.length());
        value += 1;
        jedis.hset(key, value, value);
        System.out.println(jedis.objectEncoding(key) + " " + value + " " + value.length());
    }

}
