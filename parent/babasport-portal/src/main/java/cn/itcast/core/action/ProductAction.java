package cn.itcast.core.action;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.ProductService;

/**
 * 商品操作控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class ProductAction {

	@Autowired
	private ProductService productService;

	// 显示单个商品的详情页
	@RequestMapping(value = "/product/detail")
	public String showSingleProduct(Model model, Long productId) {
		System.out.println(" 显示单个商品的详情页");
		System.out.println("productId:" + productId);

		// 根据productId从数据库中找出对应的该商品的信息
		SuperPojo superPojo = productService.findById(productId);

		Product product = (Product) superPojo.get("product");

		System.out.println("p name:" + product.getName());

		// 去除颜色的重复
		List<SuperPojo> skus = (List<SuperPojo>) superPojo.get("skus");

		Map<Long, String> colors = new TreeMap<Long, String>();

		for (SuperPojo sku : skus) {
			Long colorId = Long.parseLong(sku.get("color_id") + "");
			String colorName = (String) sku.get("colorName");
			colors.put(colorId, colorName);
		}

		superPojo.setProperty("colors", colors);

		model.addAttribute("superPojo", superPojo);

		return "product";
	}

}
