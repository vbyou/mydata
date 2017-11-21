package cn.itcast.core.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.itcast.core.tools.WebTool;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 静态化服务实现类
 * 
 * @author Administrator
 *
 */
@Service("staticPageService")
public class StaticPageServiceImpl
		implements StaticPageService, ServletContextAware {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	private ServletContext servletContext;

	@Override
	public void staticProductPage(Map<String, Object> rootMap, String id)
			throws IOException, TemplateException {

		// 获得freemarker配置对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();

		// 获得模版对象
		Template template = configuration.getTemplate("product.html");

		String path = servletContext
				.getRealPath("/html/product/" + id + ".html");

		System.out.println("path:" + path);

		File file = new File(path);

		// 如果父目录 不存在，就自己创建
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}

		// 设置生成之后的静态文件的路径
		Writer out = new FileWriter(new File(path));
		
		template.process(rootMap, out);
		

		
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
