package com.redis.delayed.queue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Application {




    public static void main(String[] args) throws URISyntaxException, IOException {
        URL url = new URL("https://preview.qiantucdn.com/58pic/40/89/45/44S581PICQifXtvJBr3ZxH_origin_PIC2018.jpg!kuan320");
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        System.out.println(is);
    }


//    public static void main(String[] args) {
//
//        Publisher publisher = new Publisher(JedisBuilder.buildJedis());
//        String listKey = "redis-delayed-queue";
//
//        // 三秒
//        publisher.pub(listKey, "3", 3);
//        // 五秒
//        publisher.pub(listKey, "5", 5);
//        // 10秒
//        publisher.pub(listKey, "10", 10);
//
//
//        Subscriber subscriber = new Subscriber(JedisBuilder.buildJedis());
//        new Thread(() -> {
//            while (true) {
//                try {
//                    String value = subscriber.subLua(listKey);
//                    if (value != null) {
//                        System.out.println(Thread.currentThread().getName() + ", 订阅结果: " + value + ", 时间: " + date());
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }, "消费者-" + 1).start();
//
//    }


    public static String date() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
