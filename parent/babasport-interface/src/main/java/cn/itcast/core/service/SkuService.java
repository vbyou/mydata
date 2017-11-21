package cn.itcast.core.service;

import java.util.List;

import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

/**
 * 库存服务接口
 * 
 * @author Administrator
 *
 */
public interface SkuService {

	/**
	 * 根据商品id查询出该商品旗下的所有库存
	 * 
	 * @param productId
	 * @return
	 */
	public List<SuperPojo> findByProductId(Long productId);

	/**
	 * 更新sku对象
	 * 
	 * @param sku
	 *            id：条件 其它属性是更新的新值
	 */
	public void update(Sku sku);

}
