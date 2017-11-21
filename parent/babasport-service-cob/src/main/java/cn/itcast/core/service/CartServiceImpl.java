package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.SuperPojo;
import redis.clients.jedis.Jedis;

/**
 * 购物车服务实现类
 * 
 * @author Administrator
 *
 */
@Service("cartService")
public class CartServiceImpl implements CartService {

	@Autowired
	private SkuDAO skuDAO;

	@Autowired
	private Jedis jedis;

	@Override
	public Cart fillItemsSkus(Cart cart) {

		if (cart == null) {
			return null;
		}

		List<Item> items = cart.getItems();

		for (Item item : items) {
			SuperPojo sku = skuDAO
					.findSKuAndColorAndProductBySkuId(item.getSkuId());
			item.setSku(sku);
		}

		return cart;
	}

	@Override
	public Cart getCartFormRedis(String username) throws JsonParseException, JsonMappingException, IOException {
		
		String cartJson = jedis.get("cart:" + username);
		
		if(cartJson==null)
		{
			return null;
		}
		
		ObjectMapper om = new ObjectMapper();
		Cart cart = om.readValue(cartJson, Cart.class);
		
		return cart;
	}

	@Override
	public void addCartToRedis(String username, Cart cart)
			throws JsonProcessingException {

		ObjectMapper om = new ObjectMapper();
		String cartJson = om.writeValueAsString(cart);

		jedis.set("cart:" + username, cartJson);

	}

}
