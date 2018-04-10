package com.go2smartphone.paidui.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RestaurantData {

	public static RestaurantData instance;
	
	
	@SerializedName("shop")
	public Shop_ shop;

	@SerializedName("staff")
	public List<Staff> staffs;

	@SerializedName("shop_queue")
	public List<ShopQueue> shopQueue;
	
	public static class Shop_ extends Shop {
		@SerializedName("logo")
		public String logo;
	}

}
