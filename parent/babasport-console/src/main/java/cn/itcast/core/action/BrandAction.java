package cn.itcast.core.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.common.URL;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.service.BrandService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 品牌管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class BrandAction {

	@Autowired
	private BrandService brandService;
	
	private int num = 100;

	// 通用的brand
	@RequestMapping(value = "/console/brand/{pageName}.do")
	public String consoleBrandShow(Model model,
			@PathVariable(value = "pageName") String pageName) {
		System.out.println("通用brand：" + pageName);

		return "brand/" + pageName;
	}

	// （特殊）显示品牌列表查询
	@RequestMapping(value = "/console/brand/list.do")
	public String consoleBrandShowList(Model model, String name,
			Integer isDisplay, Integer pageNum, Integer pageSize) throws InterruptedException {
		System.out.println("（特殊）显示品牌列表查询");
		
		System.out.println(Thread.currentThread().getName());
		
		num++;
		

		// 品牌列表的简单查询
		name = Encoding.encodeGetRequest(name);

		System.out.println("name" + name);
		System.out.println("isDisplay" + isDisplay);

		// 封装查询条件
		Brand brand = new Brand();
		brand.setName(name);
		brand.setIsDisplay(isDisplay);

		Page<Brand> pageBrands =

				brandService.findByExample(brand, pageNum, pageSize);

		System.out.println("brand size:" +

				pageBrands.getResult().size());

		// 将数据丢给页面显示
		model.addAttribute("pageBrands", pageBrands);

		// 回显查询条件
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
		
		Thread.sleep(2000);
		
		
		num--;
		
		System.out.println(num);

		return "brand/list";
	}

	// 显示修改页面
	@RequestMapping(value = "/console/brand/showEdit.do")
	public String consoleBrandShowEdit(Model model, Long brandId) {
		System.out.println("显示修改页面");
		System.out.println("brandId:" + brandId);

		// 根据品牌id，去数据中查询出对应的品牌信息，并丢给页面显示
		Brand brand = brandService.findById(brandId);

		System.out.println(brand);

		model.addAttribute("brand", brand);

		return "brand/edit";
	}

	// 执行修改
	@RequestMapping(value = "/console/brand/doEdit.do")
	public String consoleBrandDoEdit(Model model, Brand brand) {
		System.out.println("执行修改");
		System.out.println(brand);

		// 将brand的内容更新到数据库中
		brandService.update(brand);

		return "redirect:list.do";
	}

	// 执行删除
	@RequestMapping(value = "/console/brand/doDelete.do")
	public String consoleBrandDoDelete(String ids)
			throws UnsupportedEncodingException {
		System.out.println("执行删除");
		System.out.println("ids:" + ids);

		String name = "莲";

		// 根据ids去数据库中删除对应的品牌
		brandService.deleteByIds(ids);

		//name = new String(name.getBytes("UTF-8"), "ISO8859-1");
		name = URLEncoder.encode(name, "UTF-8");

		return "redirect:list.do?name=" + name;
	}

}
