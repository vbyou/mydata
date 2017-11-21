package cn.itcast.core.service;

import java.io.IOException;
import java.util.Map;

import freemarker.template.TemplateException;

/**
 * 静态化的服务接口
 * 
 * @author Administrator
 *
 */
public interface StaticPageService {
	
	/**
	 * 静态化商品详情页面
	 * @param rootMap  数据模型
	 * @param id 商品id
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public void staticProductPage(Map<String,Object> rootMap,String id) throws IOException, TemplateException;

}
