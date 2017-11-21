package cn.itcast.core.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.service.CartService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.SessionTool;

/**
 * 购物车控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class CartAction {

	@Autowired
	private CartService cartService;

	@Autowired
	private SessionService sessionService;

	// 显示购物车页面
	@RequestMapping(value = "/cart")
	public String showCart(Model model, HttpServletRequest request,
			HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {

		System.out.println("显示购物车页面");

		Cart cart1 = null; // cookie中的cart
		Cart cart2 = null; // redis中的cart
		Cart cart = null; // 合并之后的cart

		// 从cookie中获得cart
		cart1 = this.getCartFormCookies(request);

		// 判断用户是否登录
		String maosessionID = SessionTool.getSessionID(request, response);
		String username = sessionService.getUsernameForRedis(maosessionID);
		if (username != null) {
			// 从redis中取出cart
			cart2 = cartService.getCartFormRedis(username);
		}

		// 合并两处cart
		cart = this.mergeCart(cart1, cart2);

		// 判断用户是否登录
		if (username != null) {
			// 将新cart反存到redis中
			cartService.addCartToRedis(username, cart);

			// 将cookie中cart删除掉
			this.delCartFormCookies(response);
		}

		System.out.println(cart);

		// 自我填充cart对象中的item的sku
		cart = cartService.fillItemsSkus(cart);

		// 将cart丢给页面显示
		model.addAttribute("cart", cart);

		return "cart";
	}

	// 加入购物车
	@RequestMapping(value = "/addCart")
	public String addCart(Integer amount, Long skuId, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("加入购物车");
		System.out.println("skuId:" + skuId);
		System.out.println("amount:" + amount);

		// 从cookie中获得cart
		Cart cart = this.getCartFormCookies(request);

		// 判断用户是否登录
		String maosessionID = SessionTool.getSessionID(request, response);
		String username = sessionService.getUsernameForRedis(maosessionID);
		if (username != null) {
			// 从redis中取出cart
			cart = cartService.getCartFormRedis(username);
		}

		if (cart == null) {
			cart = new Cart();
		}

		// 将新item添加到新cart中
		Item item = new Item();
		item.setSkuId(skuId);
		item.setAmount(amount);

		cart.addItem(item);

		if (username != null) {
			// 将cart反存到redis
			cartService.addCartToRedis(username, cart);
		} else {
			// 将新cart反存到cookie中
			this.addCartToCookies(cart, response);
		}

		return "redirect:/cart";

	}

	// 显示结算页面
	@RequestMapping(value = "/buyer/trueBuy")
	// 结算 skuIds表示用户从购物车中选择的商品，本次课程先不做该功能
	public String trueBuy(Long skuIds, Model model, HttpServletRequest request,
			HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {

		String maosessionID = SessionTool.getSessionID(request, response);
		String username = sessionService.getUsernameForRedis(maosessionID);

		Cart cart = cartService.getCartFormRedis(username);

		// 判断redis里的购物车不能为空，也不能是空车子
		if (cart != null && cart.getItems().size() > 0) {

			// 自我填充cart
			cart = cartService.fillItemsSkus(cart);

			List<Item> items = cart.getItems();

			boolean flag = true;

			for (Item item : items) {

				// 该商品无货
				if (item.getAmount() > Integer
						.parseInt(item.getSku().get("stock") + "")) {
					// 返回购物车页面，并提示该商品无货
					item.setIsHave(false);
					flag = false;
				}

			}

			// 至少有一个商品无货
			if (!flag) {
				model.addAttribute("cart", cart);
				return "cart";
			}

		} else {
			// 继续回到购物车页面
			return "redirect:/cart";
		}

		return "order";
	}

	// 从cookie中获得cart
	public Cart getCartFormCookies(HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {

				if (cookie.getName().equals("cart")) {
					String cartJson = cookie.getValue();

					ObjectMapper om = new ObjectMapper();
					Cart cart = om.readValue(cartJson, Cart.class);

					return cart;
				}

			}
		}

		return null;
	}

	// 将新cart反存到cookie中
	public void addCartToCookies(Cart cart, HttpServletResponse response)
			throws JsonProcessingException {

		ObjectMapper om = new ObjectMapper();
		String cartJson = om.writeValueAsString(cart);

		Cookie cookie = new Cookie("cart", cartJson);
		cookie.setMaxAge(60 * 60 * 24 * 365);
		cookie.setPath("/");

		response.addCookie(cookie);

	}

	// 合并两处cart
	public Cart mergeCart(Cart cart1, Cart cart2) {
		if (cart1 == null) {
			return cart2;
		} else if (cart2 == null) {
			return cart1;
		} else {
			List<Item> items = cart1.getItems();
			for (Item item : items) {
				cart2.addItem(item);
			}
			return cart2;
		}
	}

	// 将cookie中cart删除掉
	public void delCartFormCookies(HttpServletResponse response) {
		Cookie cookie = new Cookie("cart", "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
