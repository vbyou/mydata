package cn.itcast.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;

import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

/**
 * 库存服务实现类
 * 
 * @author Administrator
 *
 */
@Service("skuService")
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuDAO skuDAO;

	@Override
	public List<SuperPojo> findByProductId(Long productId) {
		// TODO Auto-generated method stub
//		Example example = new Example(Sku.class);
//		// 设置查询条件
//		example.createCriteria().andEqualTo("productId", productId);
		
		List<SuperPojo> skus = skuDAO.findSKuAndColorByProductId(productId);

		return skus;
	}

	@Override
	public void update(Sku sku) {

		// updateByPrimaryKeySelective 表示选择性的修改，只会修改对象中有值的属性到数据库中
		skuDAO.updateByPrimaryKeySelective(sku);

	}

}
