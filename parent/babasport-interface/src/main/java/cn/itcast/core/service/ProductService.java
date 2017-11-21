package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 商品服务接口
 * 
 * @author Administrator
 *
 */
public interface ProductService {

	/**
	 * 根据案例条件查询商品
	 * 
	 * @param product
	 * @return
	 */
	public Page<Product> findByExample(Product product, Integer pageNum,
			Integer pageSize);

	/**
	 * 查询所有可用颜色 父id不为0的颜色
	 * 
	 * @return
	 */
	public List<Color> findEnableColors();
	
	/**
	 * 将商品对象添加到数据库中，同时库存表也要对应的进行添加
	 * @param product
	 */
	public void add(Product product);
	
	/**
	 * 批量修改商品的统一信息
	 * @param product
	 * @param ids
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void update(Product product,String ids) throws SolrServerException, IOException;

	/**
	 * 根据商品id 查询出该商品相关信息
	 * @param product
	 * @return
	 */
	public SuperPojo findById(Long productId);
	
}
