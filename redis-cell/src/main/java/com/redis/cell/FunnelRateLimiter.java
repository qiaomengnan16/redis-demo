package com.redis.cell;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FunnelRateLimiter {

    static class Funnel {

        // 漏斗容量
        int capacity;
        // 漏嘴流水速率
        float leakingRate;
        // 漏斗剩余空间
        int leftQuota;
        // 上一次漏水时间
        long leakingTs;

        public Funnel(int capacity, float leakingRate) {
            this.capacity = capacity;
            this.leakingRate = leakingRate;
            this.leftQuota = capacity;
            this.leakingTs = System.currentTimeMillis();
        }

        synchronized void makeSpace() {
            // 当前时间
            long nowTs = System.currentTimeMillis();
            // 距离上一次漏水过去了多长时间
            long deltaTs = nowTs - leakingTs;
            // 过去的时间流了多少水
            int deltaQuota = (int) (deltaTs * leakingRate);
            // 小于0说明时间过长，超出最大值了，重新初始化容量
            if (deltaQuota < 0) {
                this.leakingRate = capacity;
                this.leakingTs = nowTs;
                return;
            }
            // 流出的还没到1，就不往下走了
            if (deltaQuota < 1) {
                return;
            }
            // 剩余空间大于0，但是和流出的相加超出最大值后，也重新初始化容量
            if (this.leftQuota > 0 && (this.leftQuota + deltaQuota) < 0) {
                this.leakingRate = capacity;
                this.leakingTs = nowTs;
                return;
            }
            // 将剩余容量和流出的累加
            this.leftQuota += deltaQuota;
            // 记录本次流水时间
            this.leakingTs = nowTs;
            // 超出最大值，则用最大值
            if (this.leftQuota > this.capacity) {
                this.leftQuota = this.capacity;
            }
        }

        synchronized boolean watering(int quota) {
            // 计算流水和剩余容量
            makeSpace();
            // 剩余容量大于等于本次的用量，允许操作
            if (this.leftQuota >= quota) {
                this.leftQuota -= quota;
                return true;
            }
            return false;
        }

    }

    private static Map<String, Funnel> funnelMap = new ConcurrentHashMap<>();

    public static boolean isActionAllowed(String userId, String actionKey, int capacity, float leakingRate) {
        String key = String.format("%s:%s", userId, actionKey);
        Funnel funnel = funnelMap.get(key);
        if (funnel == null) {
            funnel = new Funnel(capacity, leakingRate);
            funnelMap.put(key, funnel);
        }
        return funnel.watering(1);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(isActionAllowed("mn", "reply", 5, 1));
        }
    }

}
