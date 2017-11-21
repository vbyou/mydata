package cn.itcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;


/**
 * Junit + Spring
 * 
 * @author Administrator
 * 这样就不用写代码来加载applicationContext.xml和getBean了
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestRedis {
	
	@Autowired
	private Jedis jedis;

	@Test
	public void testRedis() {

//		Jedis jedis = new Jedis("192.168.56.101", 6379);
//
//		Long incr = jedis.incr("pno");
//		System.out.println(incr);

	}
	
	@Test
	public void testRedis2() {
		// TODO Auto-generated constructor stub
		
		Long incr = jedis.incr("pno");
		System.out.println(incr);
	}
	
	

}
