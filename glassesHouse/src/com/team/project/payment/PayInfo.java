package com.team.project.payment;

public class PayInfo {

	private PayOption payOption;
	private int price;
	private boolean status;
	
	protected PayInfo() {
	}
	
	protected PayInfo(PayOption payOption, int price, boolean status) {
		this.payOption = payOption;
		this.price = price;
		this.status = status;
	}

	public PayOption getPayOption() {
		return payOption;
	}

	public void setPayOption(PayOption payOption) {
		this.payOption = payOption;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "payOption=" + payOption
				+ "\nprice=" + price
				+ "\nstatus=" + status;
	}
}
