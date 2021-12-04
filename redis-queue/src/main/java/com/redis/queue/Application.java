package com.redis.queue;

public class Application {


    public static void main(String[] args) {

        Publisher publisher = new Publisher(JedisBuilder.buildJedis());
        String listKey = "notify-queue";

        new Thread(() -> {
            int i = 0;
            while (true) {
                publisher.pub(listKey, i+"");
                i++;
            }
        }).start();


        for (int i = 0; i < 5; i++) {
            Subscriber subscriber = new Subscriber(JedisBuilder.buildJedis());
            new Thread(() -> {
                while (true) {
                    try {
                        String value = subscriber.sub(1, listKey);
                        System.out.println(Thread.currentThread().getName() + ", 订阅结果: " + value);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, "消费者-" + i).start();
        }

    }

}
