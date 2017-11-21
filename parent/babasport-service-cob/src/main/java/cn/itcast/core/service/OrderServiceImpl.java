package cn.itcast.core.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.dao.DetailDAO;
import cn.itcast.core.dao.OrderDAO;
import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Detail;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.Order;
import cn.itcast.core.tools.SessionTool;
import redis.clients.jedis.Jedis;

/**
 * 订单服务实现类
 * 
 * @author Administrator
 *
 */
@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private DetailDAO detailDAO;

	@Autowired
	private CartService cartService;

	@Autowired
	private Jedis jedis;

	@Override
	public void addOrderAndDetail(Order order, String username)
			throws JsonParseException, JsonMappingException, IOException {

		// 生成订单id
		Long oid = jedis.incr("oid");
		order.setId(oid);

		// 从redis中取出对应购物车
		Cart cart = cartService.getCartFormRedis(username);

		// 填充购物车
		cart = cartService.fillItemsSkus(cart);

		// 从购物车中取出相关信息放入订单对象中
		order.setDeliverFee(cart.getFee());
		order.setTotalPrice(cart.getTotalPrice());
		order.setOrderPrice(cart.getProductPrice());

		// 设置订单的支付状态
		// 支付状态 :0到付1待付款,2已付款,3待退款,4退款成功,5退款失败
		if (order.getPaymentWay() == 1) {
			order.setIsPaiy(0);
		} else {
			order.setIsPaiy(1);
		}

		// 设置订单状态
		// 订单状态 0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货
		order.setOrderState(0);

		// 设置时间
		order.setCreateDate(new Date());

		// 设置用户id
		// 前台注册的时候可以将用户名和用户id保存到redis中，key：用户名
		// value：用户id，方便根据用户名获得用户id
		String buyerId = jedis.get(username);
		order.setBuyerId(Long.parseLong(buyerId));

		// 订单表添加一条记录
		orderDAO.insert(order);

		// 订单详情表中添加N条记录
		List<Item> items = cart.getItems();

		for (Item item : items) {
			Detail detail = new Detail();
			detail.setOrderId(oid);

			detail.setProductId(
					Long.parseLong(item.getSku().get("product_id").toString()));
			detail.setProductName(item.getSku().get("productName").toString());
			detail.setColor(item.getSku().get("colorName").toString());
			detail.setSize(item.getSku().get("size").toString());
			detail.setPrice(
					Float.parseFloat(item.getSku().get("price").toString()));
			
			detail.setAmount(item.getAmount());

			detailDAO.insert(detail);
		}
		
		
		// 清空购物车
		jedis.del("cart:" + username);
	}

}
