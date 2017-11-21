package cn.itcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

public class TestRedis2 {

	public static void main(String[] args) {

		// 创建分片信息对象
		JedisShardInfo s1 = new JedisShardInfo("192.168.56.101", 6379);
		JedisShardInfo s2 = new JedisShardInfo("192.168.56.102", 6379);

		// 将分片信息对象添加到集合中
		List<JedisShardInfo> al = new ArrayList<JedisShardInfo>();
		al.add(s1);
		al.add(s2);

		// 创建分片jedis对象 操作几乎完全等同于jedis对象
		ShardedJedis shardedJedis = new ShardedJedis(al);
		
		for (int i = 0; i < 40; i++) {

			// 存数据到分片中
			//shardedJedis.set("damao" + i, "haha" + i);

			// 从各个分片中取得数据  damao1
			System.out.println(shardedJedis.get("damao"+i));
		}
		
		shardedJedis.close();

	}

}
