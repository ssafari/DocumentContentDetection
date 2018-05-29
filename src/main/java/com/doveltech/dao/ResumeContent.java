package com.doveltech.dao;

import java.util.ArrayList;
import java.util.List;

public class ResumeContent {

	private String firstName;
	private String lastName;
	
	private Address address;
	
	private String email;
	private String phone;
	
	private List<Company> companies;
	private List<String> skills;
	
	private List<String> certificates;
	
	
	public ResumeContent() {
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

	public void setCompany(String name, String duration) {
		if (companies == null) 
			companies = new ArrayList<Company>();
		Company cp = new Company(name, duration);
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
	
	private static class Address {
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
	
	private static class Company {
		String name;
		String duration;
		
		public Company(String name, String duration) {
			super();
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
	}
	
	
}
