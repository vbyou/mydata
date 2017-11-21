package cn.itcast.core.dao;

import java.util.List;

import cn.itcast.core.pojo.Brand;

/**
 * 品牌DAO
 * 
 * @author Administrator
 *
 */
public interface BrandDAO {

	/**
	 * 根据案例条件查询品牌
	 * 
	 * @param brand
	 * @return
	 */
	public List<Brand> findByExample(Brand brand);
	
	/**
	 * 根据品牌id查询单个的品牌对象
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

}
