package cn.itcast.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.container.page.PageHandler;

import cn.itcast.core.dao.BrandDAO;
import cn.itcast.core.pojo.Brand;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;
import redis.clients.jedis.Jedis;

/**
 * 品牌服务实现类
 * 
 * @author Administrator
 *
 */
@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Autowired
	private Jedis jedis;

	@Autowired
	private BrandDAO brandDAO;

	@Override
	public Page<Brand> findByExample(Brand brand, Integer pageNum,
			Integer pageSize) {

		PageHelper.startPage(pageNum, pageSize);// 开始分页
		brandDAO.findByExample(brand);
		Page<Brand> endPage = PageHelper.endPage();// 结束分页

		return endPage;
	}

	@Override
	public Brand findById(Long brandId) {

		return brandDAO.findById(brandId);
	}

	@Override
	public void update(Brand brand) {
		brandDAO.update(brand);

		// 同步修改到redis
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
	}

	@Override
	public void deleteByIds(String ids) {

		ids = ids.replaceAll(" ", "");

		brandDAO.deleteByIds(ids);
	}

	@Override
	public List<Brand> findAllFromRedis() {

		Map<String, String> hgetAll = jedis.hgetAll("brand");

		List<Brand> brands = new ArrayList<Brand>();
		
		for (Entry<String, String> entry : hgetAll.entrySet()) {
			Long brandId = Long.parseLong(entry.getKey());//id的值
			String name = entry.getValue();//name的值
			
			Brand brand = new Brand();
			brand.setId(brandId);
			brand.setName(name);
			brands.add(brand);
		}

		return brands;
	}

}
