package cn.itcast;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.pojo.Buyer;
import cn.itcast.core.pojo.SuperPojo;

public class TestJson {
	public static void main(String[] args)
			throws JsonGenerationException, JsonMappingException, IOException {

		// 对象转json
//		Buyer buyer = new Buyer();
//		buyer.setUsername("大毛");
//		buyer.setPassword("123456");
		
		SuperPojo superPojo = new SuperPojo();
		superPojo.setProperty("haha", "wahaha");
		superPojo.setProperty("hehe", null);

		ObjectMapper om = new ObjectMapper();
//		Writer w = new StringWriter();
//		om.writeValue(w, buyer);
//		System.out.println(w.toString());
		
		// 设置规则 排除null（注意对map无效）
		om.setSerializationInclusion(Include.NON_NULL);
		
		
		String writeValueAsString = om.writeValueAsString(superPojo);
		System.out.println(writeValueAsString);
		
		
		
		//json字符串转对象
		SuperPojo buyer2 = om.readValue(writeValueAsString, SuperPojo.class);
		
		System.out.println(buyer2.get("haha"));

	}

}
