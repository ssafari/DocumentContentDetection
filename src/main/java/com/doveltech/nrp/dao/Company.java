package com.doveltech.nrp.dao;

public class Company {
	private String title;
	private String name;
	private String sdate;
	private String edate;
	
	public Company(String name, String title, String edate, String sdate) {
		super();
		this.title = title;
		this.name = name;
		this.sdate = sdate;
		this.edate = edate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
}
