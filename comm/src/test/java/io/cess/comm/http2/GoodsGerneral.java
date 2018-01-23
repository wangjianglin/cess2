package io.cess.comm.http2;

public class GoodsGerneral{
	
	private long id;
	private String goodsName;
	private String goodsPhoto;
	private String goodsPrice;
	private String goodsMbPrice;
	private int goodsStatus;
	private String goodsVideo;
	private String goodsVideoImg;

	public long getId() {
		return id;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public String getGoodsPhoto() {
		return goodsPhoto;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public String getGoodsMbPrice() {
		return goodsMbPrice;
	}

	public int getGoodsStatus() {
		return goodsStatus;
	}

	public String getGoodsVideo() {
		return goodsVideo;
	}

	public String getGoodsVideoImg() {
		return goodsVideoImg;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setGoodsPhoto(String goodsPhoto) {
		this.goodsPhoto = goodsPhoto;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public void setGoodsMbPrice(String goodsMbPrice) {
		this.goodsMbPrice = goodsMbPrice;
	}

	public void setGoodsStatus(int goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public void setGoodsVideo(String goodsVideo) {
		this.goodsVideo = goodsVideo;
	}

	public void setGoodsVideoImg(String goodsVideoImg) {
		this.goodsVideoImg = goodsVideoImg;
	}

}
