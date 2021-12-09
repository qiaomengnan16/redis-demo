package com.redis.bloom;

import io.rebloom.client.Client;

public class App {

    public static void main(String[] args) {
        Client client = new Client("localhost", 6379);
        String key = "bloom";
        client.delete(key);
        for (int i = 0; i < 10000; i++) {
            String data = "user" + i;
            client.add(key, data);
            // 判断元素是否存在，不存在则输出
            if (client.exists(key, "user" + (i+1))) {
                System.out.println(i);
                break;
            }
        }
        client.close();
    }

}
