package com.go2smartphone.paidui.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterResult implements Serializable{
	@SerializedName("id")
	public Long id;

	@SerializedName("shop_id")
	public Long shop_id;

	@SerializedName("name")
	public String name;

	@SerializedName("is_master")
	public Boolean device_register;

	@SerializedName("version")
	public String version;

	@SerializedName("access_code")
	public String access_code;

	@SerializedName("salt")
	public String salt;

	@SerializedName("ip")
	public String ip;

	@SerializedName("uuid")
	public String uuid;

	@SerializedName("state")
	public Integer state;

	@SerializedName("create_on")
	public String create_on;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getDevice_register() {
		return device_register;
	}

	public void setDevice_register(Boolean is_master) {
		this.device_register = is_master;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAccess_code() {
		return access_code;
	}

	public void setAccess_code(String access_code) {
		this.access_code = access_code;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreate_on() {
		return create_on;
	}

	public void setCreate_on(String create_on) {
		this.create_on = create_on;
	}
}
