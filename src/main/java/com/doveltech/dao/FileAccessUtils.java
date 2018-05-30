package com.doveltech.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public final class FileAccessUtils {

	/**
	 * 
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	public static String readFileToString(String name) throws IOException {
		String str = FileUtils.readFileToString(retrieveFile(name), "UTF-8");
		//System.out.println(str.replaceAll("[^A-Za-z0-9\\r\\n ]", ""));
		return str;
	}
	
	public static File retrieveFile(String model) {
		String path = System.getProperty("user.dir")+"/models/"+model;
		System.out.println(path);
		return new File(path);
	}
	
	public static List<String> getBufferLine(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		

		Pattern p = Pattern.compile("[a-zA-Z0-9]");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		List<String> doc = new ArrayList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			if (p.matcher(line).find()) {
				
				System.out.println(line.replaceAll("[^A-Za-z0-9\\. ]", ""));
				doc.add(line.replaceAll("[^A-Za-z0-9,\\. ]", ""));
			}
		}
		br.close();
		return doc;
	}
	
}
