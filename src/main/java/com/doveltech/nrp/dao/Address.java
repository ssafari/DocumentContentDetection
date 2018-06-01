package com.doveltech.nrp.dao;

public class Address {
	private String street;
	private String state;
	private String unit;
	private String city;
	private String country;
	private String code;
	
	public Address(String street, String state, String unit, String city, String country, String code) {
		super();
		this.street = street;
		this.state = state;
		this.unit = unit;
		this.city = city;
		this.country = country;
		this.code = code;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
