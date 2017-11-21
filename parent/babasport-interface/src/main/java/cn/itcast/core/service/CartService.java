package cn.itcast.core.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.pojo.Cart;

/**
 * 购物车服务接口
 * @author Administrator
 *
 */
public interface CartService {
	
	/**
	 * cart对象的自我填充
	 * @param cart  残缺补全的cart 里面没有复合型sku对象
	 * @return  完整cart 
	 */
	public Cart fillItemsSkus(Cart cart);
	
	
	/**
	 * 从redis中取得购物车信息
	 * 
	 * @param username
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Cart getCartFormRedis(String username) throws JsonParseException, JsonMappingException, IOException;
	
	
	/**
	 * 将购物车信息添加到Redis中
	 * 
	 * @param username
	 * @param cart
	 * @throws JsonProcessingException 
	 */
	public void addCartToRedis(String username,Cart cart) throws JsonProcessingException;
}
