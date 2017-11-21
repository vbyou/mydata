package cn.itcast.core.service;

/**
 * session服务接口
 * 
 * @author Administrator
 *
 */
public interface SessionService {
	
	/**
	 * 保存用户名到redis中
	 * 
	 * @param key  uuid
	 * @param value
	 */
	public void addUsernameToRedis(String key, String value);
	
	/**
	 * 从redis中取出用户名
	 * 
	 * @param key
	 * @return
	 */
	public String getUsernameForRedis(String key);

}
