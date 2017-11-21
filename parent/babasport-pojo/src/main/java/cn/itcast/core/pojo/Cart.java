package cn.itcast.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 购物车
 * 
 * @author Administrator
 *
 */
public class Cart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 购买项的集合
	private List<Item> items = new ArrayList<Item>();

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	/**
	 * 添加新的购买项到cart list《item》中
	 * 
	 * @param item
	 */
	public void addItem(Item item) {
		// 如果是相同的购买项就叠加数量,不同的购买项就另起一行
		for (Item item2 : items) {
			if (item2.getSkuId().equals(item.getSkuId())) {
				item2.setAmount(item2.getAmount() + item.getAmount());
				return;
			}
		}
		items.add(item);
	}

	/**
	 * 获得商品总数量
	 * 
	 * @return
	 */
	//不参与json转换
	@JsonIgnore
	public Integer getProductAmount() {
		Integer result = 0;

		for (Item item : items) {
			result = result + item.getAmount();
		}
		return result;
	}

	/**
	 * 获得商品总金额
	 * 
	 * @return
	 */
	@JsonIgnore
	public Float getProductPrice() {
		Float result = 0f;

		for (Item item : items) {
			result = result + item.getAmount()
					* Float.parseFloat(item.getSku().get("price") + "");
		}
		return result;
	}
	
	/**
	 * 计算运费
	 * 
	 * @return
	 */
	@JsonIgnore
	public Float getFee() {
		Float result = 0f;
		// 如果商品总金额小于79就收个10块钱，否则就不要钱
		if (this.getProductPrice() < 79) {
			result = 10f;
		}
		return result;
	}
	
	/**
	 * 计算总价格
	 * 
	 * @return
	 */
	@JsonIgnore
	public Float getTotalPrice() {
		return this.getProductPrice() + this.getFee();
	}

}
