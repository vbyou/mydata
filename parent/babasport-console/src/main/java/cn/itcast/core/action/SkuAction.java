package cn.itcast.core.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.SkuService;

/**
 * 库存管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class SkuAction {

	@Autowired
	private SkuService skuService;

	// 显示库存列表页面
	@RequestMapping(value = "/console/sku/list.do")
	public String consoleSkuShowList(Model model, Long productId) {
		System.out.println("显示库存列表页面");
		System.out.println("productId:" + productId);

		// 从数据库查询出该商品旗下的所有库存
		List<SuperPojo> skus = skuService.findByProductId(productId);

		System.out.println("skus size:" + skus.size());

		model.addAttribute("skus", skus);

		return "sku/list";
	}

	// 执行库存修改
	@RequestMapping(value = "/console/sku/update.do")
	@ResponseBody
	public String consoleSkuDoUpdate(Sku sku) {
		
		//将sku更新到数据库中
		skuService.update(sku);
		
		return null;
	}

}
