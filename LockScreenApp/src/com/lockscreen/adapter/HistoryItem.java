package com.lockscreen.adapter;

/*Developer: TAI ZHEN KAI
Project 2015*/

public class HistoryItem {

	public Integer redemtionId;
	public Integer rewardId;
	public String name;
	public String develiryAddress;
	public Integer mobileOperator;
	public Integer redemptionStatus;
	public String redemptionStatusName;
	public String redemptionDate;
	public Boolean reviewed;

	public HistoryItem(

	Integer redemtionId, Integer rewardId, String name, String develiryAddress,
			Integer mobileOperator, Integer redemptionStatus,
			String redemptionStatusName, String redemptionDate, Boolean reviewed) {
		
		this.redemtionId = redemtionId;
		this.rewardId = rewardId;
		this.name = name;
		this.develiryAddress = develiryAddress;
		this.mobileOperator = mobileOperator;
		this.redemptionStatus = redemptionStatus;
		this.redemptionStatusName = redemptionStatusName;
		this.redemptionDate = redemptionDate;
		this.reviewed = reviewed;

	}

}
