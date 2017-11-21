package cn.itcast.core.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * 自定义转换器
 * 
 * @author Administrator S 转换前的类型 T 转换后的类型
 */
public class MyConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {

		if (source != null) {
			String trim = source.trim();
			if (!trim.equals("")) {
				return trim;
			}
		}

		return null;
	}

}
