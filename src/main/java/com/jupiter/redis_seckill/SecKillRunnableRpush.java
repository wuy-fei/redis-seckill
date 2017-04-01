package com.jupiter.redis_seckill;

import java.util.UUID;

import redis.clients.jedis.Jedis;

public class SecKillRunnableRpush implements Runnable {
	Jedis jedis = new Jedis("127.0.0.1", 6379);

	public void run() {
		try {
			String userId = UUID.randomUUID().toString();

			// 1. 第一步 初始化数据
			//Long lpush = jedis.lpush("goodstore", 1+""); // 初始化数据

			int num = (int) (Math.random() * 50);
			// 如果 用户 已经 抢到了   就不能 再 抢 
			if(!"ok".equals(jedis.get("goodId:userId"+num))){
				// 2. lpop 是 原子操作 有数据返回字符串 无数据 返回 null 值
				 String result = jedis.lpop("goodstore");
				
				 if(null!=result&& !"".equals(result.trim())){
					 System.out.println("插入订单表");
					 System.out.println("修改库存");
					 // 插入 redis 用户 抢占成功 标识     key为 [商品 id  用户id 组合 ] 
					 jedis.set("goodId:userId"+num, "ok");
					 // 思考 :  记录 成功的 userId 到     redis 的 一个 set集合中  作为记录 
					 jedis.sadd("goodstore_sucessUser", userId);
				 }
				 else{
					 System.out.println("抢占失败!!!!!!!!!");
				 }
			}else{
				System.out.println("用户已经抢购过了-[不要再抢 @@]------------->!");
			}
			

			
			
			
			// jedis.watch("stock");
			// int stock = Integer.valueOf(jedis.get("stock"));
			//
			// if (stock > 0) {
			// Transaction tx = jedis.multi(); // 开启事物
			//
			// int nowStock = stock - 1;
			//
			// tx.set("stock", String.valueOf(nowStock));
			//
			// List list = tx.exec(); // 提交事务，如果此时watchKeys被改动了，则返回null
			//
			// if (null != list && !list.isEmpty()) {
			// System.out.println("用户: " + userId + " 抢购成功，当前库存: " + nowStock);
			// // set集合 无需不重复
			// jedis.sadd("successUsers", userId);
			// // 成功队列
			//
			// } else {
			// System.out.println("用户: " + userId + " 抢购失败");
			// // set集合 无需不重复
			// jedis.sadd("failureUsers", userId);
			// }
			// } else {
			// System.out.println("用户: " + userId + " 抢购失败，没库存了!");
			// jedis.sadd("failureUsers", userId);
			// }
			
			
			
		} finally {
			jedis.close();
		}
	}
}
