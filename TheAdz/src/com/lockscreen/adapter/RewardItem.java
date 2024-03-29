package com.lockscreen.adapter;

/*Developer: TAI ZHEN KAI
Project 2015*/

public class RewardItem {

	public Integer rewardId;
	public String name;
	public String description;
	public String imageId;
	public String imageName;
	public String imageUrl;
	public String imageUrlLink;
	public Integer typeId;
	public String TypeName;
	public Boolean typeDelivery;
	public Boolean typeMoney;
	public Boolean Mobile;
	public Integer pointRequired;

	public RewardItem(Integer rewardId, String name, String description,
			String imageId, String imageName, String imageUrl,
			String imageUrlLink, Integer typeId, String TypeName,
			Boolean typeDelivery, Boolean typeMoney, Boolean Mobile, Integer pointRequired) {

		this.rewardId = rewardId;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
		this.imageName = imageName;
		this.imageUrl = imageUrl;
		this.imageUrlLink = imageUrlLink;
		this.typeId = typeId;
		this.TypeName = TypeName;
		this.typeDelivery = typeDelivery;
		this.typeMoney = typeMoney;
		this.Mobile = Mobile;
		this.pointRequired = pointRequired;

	}
}
