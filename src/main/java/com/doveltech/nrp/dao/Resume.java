package com.doveltech.nrp.dao;

import java.util.ArrayList;
import java.util.List;

public class Resume {

	private String fullName;
	
	private String address;
	
	private String email;
	private String phone;
	
	private List<Company> companies;
	private List<String> skills;
	private List<String> certificates;
	
	public Resume() {
		super();
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String firstName) {
		this.fullName = firstName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
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

	public void setCompanies(Company cy) {
		if (this.companies == null)
			this.companies = new ArrayList<Company>();
		companies.add(cy);
	}
	
	public Company findCompany(String cname) {
		if (companies == null)
			return null;
		for (Company cy : companies) {
			if (cname.compareTo(cy.getName()) == 0)
				return cy;
		}
		return null;
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
