package cn.itcast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class TestRedis1 {

	public static void main(String[] args) {

		// 创建哨兵连接池
		Set<String> sentinels = new HashSet<>(
				Arrays.asList("192.168.56.101:26379"));
		JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("master",
				sentinels);
		
		// 通过哨兵连接池获得jedis
		Jedis jedis = jedisSentinelPool.getResource();
		
		String string = jedis.get("yun4");
		System.out.println(string);

	}

}
