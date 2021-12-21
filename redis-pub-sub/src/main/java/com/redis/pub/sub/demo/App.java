package com.redis.pub.sub.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class App {

    static class Pub {

        String channel;
        Jedis jedis;

        public Pub(String channel) {
            this.channel = channel;
            this.jedis = JedisBuilder.buildJedis();
        }

        public void pub() {
            new Thread(() -> {
                int i = 1;
                while (true) {
                    jedis.publish(channel, "hello" + i);
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();;

        }

    }

    static class Sub {

        String channel;
        String name;
        Jedis jedis;

        public Sub(String channel, String name) {
            this.channel = channel;
            this.name = name;
            this.jedis = JedisBuilder.buildJedis();
        }

        public void sub() {
            new Thread(() -> {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println("channel: " + channel + "name: " + name + ", message: " + message);
                    }
                }, channel);
            }).start();
        }

    }



    public static void main(String[] args) throws InterruptedException {
        // 主题名称
        String channel = "channel";
        // 消费者1
        Sub sub = new Sub(channel, "sub1");
        sub.sub();
        // 消费者2
        Sub sub2 = new Sub(channel, "sub2");
        sub2.sub();
        // 消费者3
        Sub sub3 = new Sub(channel, "sub3");
        sub3.sub();
        Thread.sleep(1000);
        // 发布者发布消息
        Pub pub = new Pub(channel);
        pub.pub();
    }

}
