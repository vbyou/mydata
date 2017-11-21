package cn.itcast.core.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.ProductService;
import cn.itcast.core.service.StaticPageService;
import freemarker.template.TemplateException;

public class MyMessageListener implements MessageListener {

	@Autowired
	private ProductService productService;

	@Autowired
	private StaticPageService staticPageService;

	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage amessage = (ActiveMQTextMessage) message;

		try {
			String ids = amessage.getText();
			System.out.println("cms ids:" + ids);

			String[] split = ids.split(",");

			for (String id : split) {

				// 根据ids去构建数据模型，并调用静态化的方法
				// 根据productId从数据库中找出对应的该商品的信息
				SuperPojo superPojo = productService
						.findById(Long.parseLong(id));

				Product product = (Product) superPojo.get("product");

				System.out.println("p name:" + product.getName());

				// 去除颜色的重复
				List<SuperPojo> skus = (List<SuperPojo>) superPojo.get("skus");

				Set<Color> colors = new HashSet<Color>();
				

				for (SuperPojo sku : skus) {
					Long colorId = Long.parseLong(sku.get("color_id") + "");
					String colorName = (String) sku.get("colorName");
					Color color = new Color();
					color.setId(colorId);
					color.setName(colorName);
					
					colors.add(color);
				}

				superPojo.setProperty("colors", colors);

				Map<String, Object> rootMap = new HashMap<String, Object>();

				rootMap.put("superPojo", superPojo);

				//执行静态化
				staticPageService.staticProductPage(rootMap, id);

			}

		} catch (JMSException | IOException | TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
