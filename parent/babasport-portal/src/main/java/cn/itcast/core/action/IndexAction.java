package cn.itcast.core.action;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.BrandService;
import cn.itcast.core.service.SolrService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 首页控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class IndexAction {

	@Autowired
	private BrandService brandService;

	@Autowired
	private SolrService solrService;

	// 显示首页
	@RequestMapping(value = "/")
	public String showIndex() {
		return "index";
	}

	// 开始搜索
	@RequestMapping(value = "/search")
	public String search(Model model, String keyword, String sort, Long brandId,
			Float pa, Float pb, Integer pageNum, Integer pageSize)
			throws SolrServerException {
		System.out.println("开始搜索");
		keyword = Encoding.encodeGetRequest(keyword);
		System.out.println("keyword:" + keyword);

		// 从redis中取出所有品牌，并丢给页面显示
		List<Brand> brands = brandService.findAllFromRedis();

		model.addAttribute("brands", brands);

		// 根据用户输入的关键字，从solr索引库中搜索出对应的商品信息，并丢给页面显示
		Page<SuperPojo> pageSuperPojos = solrService.findProductByKeyWord(
				keyword, sort, brandId, pa, pb, pageNum, pageSize);

		model.addAttribute("sort2", sort);

		// 反正sort的值
		if (sort.equals("price asc")) {
			sort = "price desc";
		} else {
			sort = "price asc";
		}
		
		//用户已经选择的内容
		Map<String, String> treeMap = new TreeMap<String,String>();
		
		
		//根据brandID从brands中找到对应的品牌名称
		for (Brand brand : brands) {
			if(brand.getId()==brandId)
			{
				treeMap.put("品牌", brand.getName());
				break;
			}
		}
		
		
		if(pa!=null&&pb!=null)
		{
			treeMap.put("价格", pa+"-"+pb);
		}
		
		model.addAttribute("map", treeMap);
	
		model.addAttribute("pageSuperPojos", pageSuperPojos);

		model.addAttribute("sort", sort);
		model.addAttribute("keyword", keyword);
		model.addAttribute("brandId", brandId);
		model.addAttribute("pa", pa);
		model.addAttribute("pb", pb);

		return "search";
	}

}
