package cn.itcast.core.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.container.page.PageHandler;
import com.github.abel533.entity.Example;

import cn.itcast.core.dao.ColorDAO;
import cn.itcast.core.dao.ProductDAO;
import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;
import redis.clients.jedis.Jedis;

/**
 * 商品服务实现类
 * 
 * @author Administrator
 *
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private ColorDAO colorDAO;

	@Autowired
	private SkuDAO skuDAO;

	@Autowired
	private Jedis jedis;

	@Autowired
	private HttpSolrServer solrServer;
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public Page<Product> findByExample(Product product, Integer pageNum,
			Integer pageSize) {

		Example example = new Example(Product.class);

		if (product.getName() == null) {
			product.setName("");
		}

		// 设置查询条件
		example.createCriteria().andLike("name", "%" + product.getName() + "%");

		// 设置排序
		example.setOrderByClause("id desc");

		PageHelper.startPage(pageNum, pageSize);
		productDAO.selectByExample(example);
		Page<Product> endPage = PageHelper.endPage();
		return endPage;
	}

	@Override
	public List<Color> findEnableColors() {

		Example example = new Example(Color.class);
		example.createCriteria().andNotEqualTo("parentId", 0);

		return colorDAO.selectByExample(example);
	}

	@Override
	public void add(Product product) {

		// 设置商品默认为不上架
		product.setIsShow(0);

		product.setCreateTime(new Date());

		// 获得redis分配的id
		Long incr = jedis.incr("pno");
		product.setId(incr);

		// 添加商品
		productDAO.insert(product);

		System.out.println("pid:" + product.getId());

		// 添加库存（N）

		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");

		for (String color : colors) {

			for (String size : sizes) {

				Sku sku = new Sku();

				sku.setProductId(product.getId());
				sku.setColorId(Long.parseLong(color));
				sku.setSize(size);

				sku.setMarketPrice(1000.00f);
				sku.setPrice(800.00f);
				sku.setDeliveFee(20f);
				sku.setStock(0);
				sku.setUpperLimit(100);

				sku.setCreateTime(new Date());

				skuDAO.insert(sku);

			}

		}

	}

	@Override
	public void update(Product product, final String ids)
			throws SolrServerException, IOException {
		
		String[] split = ids.split(",");
		for (String id : split) {

			product.setId(Long.parseLong(id));
			productDAO.updateByPrimaryKeySelective(product);

		}

		// 商品上架
		if (product.getIsShow() != null && product.getIsShow() == 1) {

			// 将上架商品的ids发送给mq
			jmsTemplate.send("productIds", new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					
					TextMessage createTextMessage = session.createTextMessage(ids);
					
					return createTextMessage;
				}
			});
			
			
		}
	}

	@Override
	public SuperPojo findById(Long productId) {

		// 商品简单信息
		// 根据商品id查询出商品对象
		Product product = productDAO.selectByPrimaryKey(productId);  //1 sql

		// 根据商品id查询出该商品旗下的所有库存
//		Sku sku = new Sku();
//		sku.setProductId(productId);
//		List<Sku> skus = skuDAO.select(sku);
		
		List<SuperPojo> skus = skuDAO.findSKuAndColorByProductId(productId);

		SuperPojo superPojo = new SuperPojo();
		superPojo.setProperty("product", product);
		superPojo.setProperty("skus", skus);

		return superPojo;
	}
}
