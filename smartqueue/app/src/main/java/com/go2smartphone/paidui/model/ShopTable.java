package com.go2smartphone.paidui.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class ShopTable {

	public ShopTable() {
	}

	public void clear() {

	}

	@SerializedName("shop_id")
	public long shop_id;

	@SerializedName("table_id")
	public long table_id;

	@SerializedName("table_seat_id")
	public long table_seat_id;

	@SerializedName("state")
	public long state;

	@SerializedName("lock")
	public long lock;

	@SerializedName("id")
	public long id;

	@SerializedName("record_state_id")
	public long record_state_id;

	@SerializedName("last_update_time")
	public long last_update_time;

	@SerializedName("device_id")
	public long device_id;

	@SerializedName("seat_name")
	public String seat_name;

	@SerializedName("name")
	public String name;

	@SerializedName("table_group_id")
	public long table_group_id;

	@SerializedName("size")
	public long size;

	@SerializedName("table_type_id")
	public long table_type_id;

	@SerializedName("bill_id")
	public long bill_id;

	@SerializedName("bill_sn")
	public long bill_sn;

	@SerializedName("person_count")
	public long person_count;

	@SerializedName("amount")
	public long amount;

	@SerializedName("create_on")
	public String create_on;

	@SerializedName("surcharge_type_id")
	public long surcharge_type_id;

	@SerializedName("cap")
	public long cap;

	@SerializedName("surcharge_id")
	public long surcharge_id;

	@SerializedName("min_charge")
	public String min_charge;

	public static ShopTable fromJson(String json) {
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.fromJson(json, ShopTable.class);
	}

}
