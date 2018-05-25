package com.doveltech.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public final class FileAccessUtils {

	/**
	 * 
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	public static String readFileToString(File name) throws IOException {
		String str = FileUtils.readFileToString(name, "UTF-8");
		return str;
	}
	
	public static File getModelFile(String model) {
		String path = System.getProperty("user.dir")+"/models/"+model;
		System.out.println(path);
		return new File(path);
	}
	
	public static List<String> getBufferLine(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		List<String> doc = new ArrayList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			doc.add(line);
		}
		br.close();
		return doc;
	}
}
