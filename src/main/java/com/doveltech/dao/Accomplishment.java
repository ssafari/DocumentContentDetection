package com.doveltech.dao;

import java.io.File;
import java.util.TreeSet;

public class Accomplishment extends AbstractFileDataAccess {

	private final String file;
	private TreeSet<String> accomplishments;
	
	public Accomplishment(String file) {
		super();
		this.file = file;
		this.accomplishments = new TreeSet<String>();
		initialize();
	}

	public void initialize() {
		File ob = getModelFile(file);
	}
	
	public TreeSet<String> getAccomplishments() {
		return accomplishments;
	}

	public boolean hasContentElement(String cnt) {
		return this.accomplishments.contains(cnt.toLowerCase());
	}

	@Override
	public void printMethod() {
		// TODO Auto-generated method stub
		
	}
}
