package com.lockscreen.adapter;

public class CampaignItem {

	public Integer campaignId;
	public String campaignName;
	public String referenceCode;
	public String startDate;
	public String endDate;
	public Integer merchantId;
	public String merchanName;
	public String subImgId;
	public String subImgName;
	public String subImgUrl;
	public String subImgUrlLink;

	public CampaignItem(Integer campaignId, String campaignName,
			String referenceCode, String startDate, String endDate,
			Integer merchantId, String merchanName, String subImgId,
			String subImgName, String subImgUrl, String subImgUrlLink) {

		this.campaignId = campaignId;
		this.campaignName = campaignName;
		this.referenceCode = referenceCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.merchantId = merchantId;
		this.merchanName = merchanName;
		this.subImgId = subImgId;
		this.subImgName = subImgName;
		this.subImgUrl = subImgUrl;
		this.subImgUrlLink = subImgUrlLink;

	}
	
	
	public CampaignItem(Integer id,String name,Integer mId, String mName, String imgName, String imgUrl){
		
		this.campaignId = id;
		this.campaignName = name;
		this.merchantId = mId;
		this.merchanName = mName;
		this.subImgName = imgName;
		this.subImgUrl = imgUrl;
	}

}