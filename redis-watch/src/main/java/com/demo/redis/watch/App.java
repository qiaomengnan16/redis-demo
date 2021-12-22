package com.demo.redis.watch;

import java.util.concurrent.CountDownLatch;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class App {

    public static void main(String[] args) throws InterruptedException {
        String key = "aData";
        Jedis jedis = JedisBuilder.buildJedis();
        jedis.set(key, "0");
        CountDownLatch count = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Jedis jedisA = JedisBuilder.buildJedis();
                while (true) {
                    jedisA.watch(key);
                    int value = Integer.parseInt(jedisA.get(key));
                    value++;
                    Transaction tx = jedisA.multi();
                    tx.set(key, value + "");
                    if (tx.exec() != null) {
                        System.out.println(Thread.currentThread().getName() + " 操作成功");
                        count.countDown();
                        break;
                    } else {
                        System.out.println(Thread.currentThread().getName() + " 发生数据冲突，更新失败，重试");
                    }
                }
            }).start();
        }
        count.await();
        System.out.println(jedis.get(key));
    }




}
