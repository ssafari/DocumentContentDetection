package com.doveltech.dao;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public abstract class AbstractFileDataAccess {

	/**
	 * 
	 * @param name
	 * @return
	 */
	public String readFileToString(File name) {
		//File file = new File("D:\\path\\report.html");
		try {
			String str = FileUtils.readFileToString(name, "UTF-8");
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public File getModelFile(String model) {
		String path = System.getProperty("user.dir")+"/models/"+model;
		System.out.println(path);
		return new File(path);
	}
	
	public abstract void printMethod();
}
