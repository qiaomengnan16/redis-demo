package com.redis.delayed.queue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Application {


    public static void main(String[] args) {

        Publisher publisher = new Publisher(JedisBuilder.buildJedis());
        String listKey = "redis-delayed-queue";

        // 三秒
        publisher.pub(listKey, "3", 3);
        // 五秒
        publisher.pub(listKey, "5", 5);
        // 10秒
        publisher.pub(listKey, "10", 10);


        Subscriber subscriber = new Subscriber(JedisBuilder.buildJedis());
        new Thread(() -> {
            while (true) {
                try {
                    String value = subscriber.subLua(listKey);
                    if (value != null) {
                        System.out.println(Thread.currentThread().getName() + ", 订阅结果: " + value + ", 时间: " + date());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, "消费者-" + 1).start();

    }


    public static String date() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
