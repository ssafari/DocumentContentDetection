package com.doveltech.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.TreeMap;


public class CommonProfilerStore {

	public static final String[] TAGS = {"",""};
	private final HashMap<String, String> missedProfiles;
	private final TreeMap<String, String> profileStore;

	public CommonProfilerStore() {
		super();
		this.profileStore = new TreeMap<String, String>();
		this.missedProfiles = new HashMap<String, String>();
	}

	private void addMissedEntityContent(String val) {
		this.missedProfiles.put(val, "missed");
	}
	
	public String findProfile(String key) {
		if (profileStore.containsKey(key.toLowerCase()))
			return this.profileStore.get(key.toLowerCase());
		addMissedEntityContent(key);
		return null;
	}
	
	/**
	 * 
	 * @param path
	 */
	public void createProfileStore(String path) {

		FilenameFilter fileFilter = new FilenameFilter() {    
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".lst")) {
					return true;
				} else {
					return false;
				}
			}
		};

		File[] files = new File(path).listFiles(fileFilter); 
		for (File file : files) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				//Construct BufferedReader from InputStreamReader
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));

				String line = null;
				while ((line = br.readLine()) != null) {
					//System.out.println(line);
					profileStore.put(line.toLowerCase(), file.getName().split("\\.")[0].toLowerCase());
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void printProfiles() {
		System.out.println(profileStore.toString());
	}
	
	public static void main(String args[]) { 
		CommonProfilerStore cProfiles = new CommonProfilerStore();
		cProfiles.createProfileStore(System.getProperty("user.dir")+"/models/");
		System.out.println("1: "+cProfiles.findProfile("academic experience"));
		System.out.println("2: "+cProfiles.findProfile("Technical Skills"));
	}
}
