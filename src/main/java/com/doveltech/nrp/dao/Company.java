package com.doveltech.nrp.dao;

public class Company {
	private String title;
	private String name;
	private String duration;
	
	public Company(String name, String duration, String title) {
		super();
		this.setTitle(title);
		this.name = name;
		this.duration = duration;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
