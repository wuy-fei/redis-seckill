package com.jupiter.redis_seckill;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redis.clients.jedis.Jedis;

public class SecKillTest {
	public static void main(String... args) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);

//        final Jedis jedis = new Jedis();
//
//        jedis.set("stock", "10"); // 重置库存为10
//        jedis.del("successUsers", "failureUsers"); // 清空抢成功和抢失败的用户集合
//        jedis.close();

        // 测试同时一千人访问
        for (int i = 0; i < 1000; i++) {
            //executorService.execute(new SecKillRunnable());
            executorService.execute(new SecKillRunnableRpush());
        }

        executorService.shutdown();
    }
}	
