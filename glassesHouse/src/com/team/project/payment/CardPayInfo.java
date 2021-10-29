package com.team.project.payment;

public class CardPayInfo extends PayInfo {

	String cardNum;

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	@Override
	public String toString() {
		return super.toString()
				+ "\ncardNum=" + cardNum;
	}
}
