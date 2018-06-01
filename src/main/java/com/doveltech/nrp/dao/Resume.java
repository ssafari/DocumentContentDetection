package com.doveltech.nrp.dao;

import java.util.ArrayList;
import java.util.List;

public class Resume {

	private String firstName;
	private String lastName;
	
	private Address address;
	
	private String email;
	private String phone;
	
	private List<Company> companies;
	private List<String> skills;
	
	private List<String> certificates;
	
	
	public Resume() {
		super();
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setCompany(String name, String duration, String title) {
		if (companies == null) 
			companies = new ArrayList<Company>();
		Company cp = new Company(name, duration, title);
		companies.add(cp);
	}

	public void setAddress(String street, String state, String unit, 
			               String city, String country, String code) {
		Address addr = new Address(street,state,unit,city,country,code);
		setAddress(addr);
	}
	
	public void setSkills(String skill) {
		if (skills == null) 
			skills = new ArrayList<String>();
		skills.add(skill);
	}
	
	public void setCertificates(String cert) {
		if (certificates == null) 
			certificates = new ArrayList<String>();
		skills.add(cert);
	}
}
