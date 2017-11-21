package cn.itcast.core.action;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.service.ProductService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 商品管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class ProductAction {

	@Autowired
	private ProductService productService;

	// 通用的product
	@RequestMapping(value = "/console/product/{pageName}.do")
	public String consoleProductShow(Model model,
			@PathVariable(value = "pageName") String pageName) {
		System.out.println("通用product：" + pageName);

		return "product/" + pageName;
	}

	// （特殊）显示商品列表查询
	@RequestMapping(value = "/console/product/list.do")
	public String consoleProductShowList(Model model, String name,
			Integer pageNum, Integer pageSize) {
		System.out.println("（特殊）显示商品列表查询");

		name = Encoding.encodeGetRequest(name);

		Product product = new Product();
		product.setName(name);

		Page<Product> pageProducts = productService.findByExample(product,
				pageNum, pageSize);

		System.out.println("psize:" + pageProducts.getResult().size());

		model.addAttribute("pageProducts", pageProducts);

		return "product/list";
	}

	// 显示商品添加页面
	@RequestMapping(value = "/console/product/showAdd.do")
	public String consoleProductShowAdd(Model model) {
		System.out.println("显示商品添加页面");

		// 读取数据库中的可用颜色 （父id不为0）
		List<Color> colors = productService.findEnableColors();

		model.addAttribute("colors", colors);

		return "product/add";
	}

	// 执行商品添加
	@RequestMapping(value = "/console/product/doAdd.do")
	public String consoleProductDoAdd(Model model, Product product) {
		System.out.println("执行商品添加");
		System.out.println(product);

		// 将商品对象添加到数据库中，同时库存表也要对应的进行添加
		productService.add(product);

		return "redirect:list.do";
	}

	// 执行商品的上下架
	@RequestMapping(value = "/console/product/isShow.do")
	public String consoleProductDoIsShow(Integer flag, String ids)
			throws SolrServerException, IOException {

		// 根据flag的值，修改ids的商品的上下架状态
		Product product = new Product();
		product.setIsShow(flag);

		productService.update(product, ids);

		return "redirect:list.do";
	}

}
