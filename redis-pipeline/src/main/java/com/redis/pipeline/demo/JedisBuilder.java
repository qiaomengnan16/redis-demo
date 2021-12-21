package com.redis.pipeline.demo;

import redis.clients.jedis.Jedis;

public class JedisBuilder {


    public static synchronized Jedis buildJedis() {

        return new Jedis("localhost", 6379);
    }

}
