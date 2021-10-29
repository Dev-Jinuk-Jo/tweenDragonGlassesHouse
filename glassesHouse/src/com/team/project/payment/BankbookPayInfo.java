package com.team.project.payment;

public class BankbookPayInfo extends PayInfo {

	String bankbookNum;
	
	
	public BankbookPayInfo() {
		this(null, 0, false, "");
	}

	public BankbookPayInfo(PayOption payOption, int price, boolean status, String bankbookNum) {
		super(payOption, price, status);
		this.bankbookNum = bankbookNum;
	}

	public String getBankbookNum() {
		return bankbookNum;
	}

	public void setBankbookNum(String bankbookNum) {
		this.bankbookNum = bankbookNum;
	}

	@Override
	public String toString() {
		return super.toString()
				+ "\nbankbookNum=" + bankbookNum;
	}
}
