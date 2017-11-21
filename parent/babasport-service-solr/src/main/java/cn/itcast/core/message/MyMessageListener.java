package cn.itcast.core.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;

import cn.itcast.core.dao.ProductDAO;
import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 自定义消息监听器类
 * 
 * @author Administrator
 *
 */
public class MyMessageListener implements MessageListener {

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private SkuDAO skuDAO;

	@Autowired
	private HttpSolrServer solrServer;

	/**
	 * 当监听到消息后，会自动调用此方法
	 */
	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage amessage = (ActiveMQTextMessage) message;

		try {
			String ids = amessage.getText();
			System.out.println("solr ids:" + ids);

			String[] split = ids.split(",");

			// 根据商品的ids，去数据库中查询出对应的变异信息，并添加到solr索引库中
			// 将上架商品的信息添加到solr索引库中
			for (String id : split) {

				// 根据id查询单个商品的信息
				Product product2 = productDAO
						.selectByPrimaryKey(Long.parseLong(id));

				SolrInputDocument document = new SolrInputDocument();

				// 商品id
				document.addField("id", id);

				// 商品名称
				document.addField("name_ik", product2.getName());

				// 品牌id
				document.addField("brandId", product2.getBrandId());

				// 首张图片
				String url = product2.getImgUrl().split(",")[0];

				// 该商品旗下的所有库存的最低价格
				// SELECT * from bbs_sku as s where s.product_id = 438 ORDER BY
				// price ASC LIMIT 0,1;
				Example example = new Example(Sku.class);
				example.createCriteria().andEqualTo("productId",
						product2.getId());
				example.setOrderByClause("price asc");
				PageHelper.startPage(1, 1);
				skuDAO.selectByExample(example);
				Page<Sku> endPage = PageHelper.endPage();

				Float price = endPage.getResult().get(0).getPrice();

				document.addField("price", price);

				document.addField("url", url);

				solrServer.add(document);
				solrServer.commit();// 提交
			}

		} catch (JMSException | SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
