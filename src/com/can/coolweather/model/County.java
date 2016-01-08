package com.can.coolweather.model;

public class County {
	private int id;
	// 县的名称
	private String countyName;
	// 县的代号
	private String countyCode;
	//城市的ID
	private int cityId;

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public County() {
		// TODO Auto-generated constructor stub
	}

	public County(int id, String countyName, String countyCode) {
		super();
		this.id = id;
		this.countyName = countyName;
		this.countyCode = countyCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

}
