package com.demo.redis.watch;

import java.util.concurrent.CountDownLatch;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class App {

    public static void main(String[] args) throws InterruptedException {
        // key
        String key = "aData";
        Jedis jedis = JedisBuilder.buildJedis();
        // 默认设置为0
        jedis.set(key, "0");
        CountDownLatch count = new CountDownLatch(10);
        // 开10个线程对key做++操作，最终值应该是10
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Jedis jedisA = JedisBuilder.buildJedis();
                while (true) {
                    // watch
                    jedisA.watch(key);
                    // 获取并自增
                    int value = Integer.parseInt(jedisA.get(key));
                    value++;
                    // 开启事务并传值
                    Transaction tx = jedisA.multi();
                    tx.set(key, value + "");
                    // 成功退出循环
                    if (tx.exec() != null) {
                        System.out.println(Thread.currentThread().getName() + " 操作成功");
                        count.countDown();
                        break;
                    } else {
                        // 不成功继续重试
                        System.out.println(Thread.currentThread().getName() + " 发生数据冲突，更新失败，重试");
                    }
                }
            }).start();
        }
        count.await();
        // 输出值
        System.out.println(jedis.get(key));
    }




}
