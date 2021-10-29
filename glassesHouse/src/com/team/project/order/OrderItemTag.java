package com.team.project.order;

import com.team.project.item.ItemTag;

public class OrderItemTag extends ItemTag {

	private long orderCode;

	public OrderItemTag() {
		this("", 0, 0, 0);
	}

	public OrderItemTag(String itemCode, int count, int price, long orderCode) {
		super(itemCode, count, price);
		this.orderCode = orderCode;
	}

	public OrderItemTag(long orderCode) {
		super();
		this.orderCode = orderCode;
	}

	public long getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(long orderCode) {
		this.orderCode = orderCode;
	}

	@Override
	public String toString() {
		return "orderCode=" + orderCode + "\n";
	}
}
