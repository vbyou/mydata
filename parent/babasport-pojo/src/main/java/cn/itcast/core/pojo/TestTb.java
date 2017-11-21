package cn.itcast.core.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 测试实体类 alt + shift + j
 * 
 * @author Administrator
 *
 */
public class TestTb implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id; // id
	private String name; // 名称
	private Date birthday; // 生日

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

}
