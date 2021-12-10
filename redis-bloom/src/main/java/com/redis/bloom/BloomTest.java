package com.redis.bloom;

import io.rebloom.client.Client;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BloomTest {

    private String chars;

    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            builder.append((char)('a' + i));
        }
        chars = builder.toString();
    }

    private String randomString(int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int idx = ThreadLocalRandom.current().nextInt(chars.length());
            builder.append(chars.charAt(idx));
        }
        return builder.toString();
    }

    private List<String> randomUsers(int n) {
        List<String> users = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            users.add(randomString(64));
        }
        return users;
    }


    public static void main(String[] args) {
        BloomTest bloom = new BloomTest();
        // 取10万个随机数
        List<String> users = bloom.randomUsers(100000);

        System.out.println(new HashSet<>(users).size());

        // 一半用于存入bloom
        List<String> userTrain = users.subList(0, users.size() / 2);
        // 一半用于判断
        List<String> userTest = users.subList(users.size() / 2 , users.size());

        Client client = new Client("localhost", 6379);
        String key = "codehole";
        client.delete(key);
        // 设置容量和错误率
        client.createFilter(key, 50000, 0.001);
        // 开始保存
        for (String user : userTrain) {
            client.add(key, user);
        }
        // 开始判断
        int falses = 0;
        for (String user : userTest) {
            boolean ret = client.exists(key, user);
            if (ret) {
                falses ++;
            }
        }

        System.out.printf("%d %d\n", falses , userTest.size());
        client.close();

    }






}
