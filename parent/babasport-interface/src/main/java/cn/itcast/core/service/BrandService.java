package cn.itcast.core.service;

import java.util.List;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 品牌服务接口
 * 
 * @author Administrator
 *
 */
public interface BrandService {

	/**
	 * 根据案例条件查询品牌
	 * 
	 * @param brand
	 * @return
	 */
	public Page<Brand> findByExample(Brand brand, Integer pageNum,
			Integer pageSize);

	/**
	 * 根据品牌id查询单个的品牌对象
	 * 
	 * @param brandId
	 * @return
	 */
	public Brand findById(Long brandId);

	/**
	 * 修改品牌对象
	 * 
	 * @param brand
	 *            id是修改的条件 其它属性是修改后的新值
	 */
	public void update(Brand brand);

	/**
	 * 根据ids删除品牌
	 * 
	 * @param ids
	 */
	public void deleteByIds(String ids);
	
	/**
	 * 从redis中查询所有品牌
	 * @return
	 */
	public List<Brand> findAllFromRedis();

}
