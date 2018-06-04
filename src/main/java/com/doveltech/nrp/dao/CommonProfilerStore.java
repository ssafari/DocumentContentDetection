package com.doveltech.nrp.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * 
 * @author sep
 *
 */
public class CommonProfilerStore {

	public static final String[] TAGS = {"",""};
	private final HashMap<String, String> missedProfiles;
	private final TreeMap<String, String> profileStore;
	private final List<String> jobTitles;

	ArrayList<String> months = new ArrayList<>(
			Arrays.asList("january", "february", "march", "april", "may", "june", "july", 
					      "august", "september", "october", "november", "december", 
					      "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", 
					      "oct", "nov", "dec"));
	
	ArrayList<String> c_entity = new ArrayList<>(
			Arrays.asList("limited.", "inc.", "ltd.", "corp.", "corporation.", "co.",
						  "limited", "inc", "ltd", "corp", "corporation", "co", "systems"));
	
	
	
	public CommonProfilerStore() {
		super();
		this.profileStore = new TreeMap<String, String>();
		this.missedProfiles = new HashMap<String, String>();
		this.jobTitles = new ArrayList<String>();
	}
	
	public String isCompanyName(String str) {
		String ln = str.replaceAll("\\s+", " ").trim();
		String[] tokens = ln.split(" ");
		String name="";
		for (int i = 0; i < tokens.length; i++) {
			name = name+" "+tokens[i];
			if (c_entity.contains(tokens[i].toLowerCase())) {
				return str;
			}
		}
		return null;
	}
	
	public boolean isDate(String str) {
		return months.contains(str.toLowerCase());
	}
	
	public int getMonthPos(String mon) {
		if (isDate(mon)) {
			int index = months.indexOf(mon.toLowerCase())+1;
			if (index < 13)
				return index;
			return (index-12);
		}
		return -1;
	}
	private void addMissedEntityContent(String val) {
		this.missedProfiles.put(val, "missed");
	}
	
	public boolean isJobTitle(String title) {
		return this.jobTitles.contains(title.toLowerCase());
	}
	
	public String findProfile(String key) {
		if (profileStore.containsKey(key.toLowerCase()))
			return this.profileStore.get(key.toLowerCase());
		if (key.split(" ").length < 4)
			addMissedEntityContent(key);
		return null;
	}
	
	public void createJobTitleData(String path) {
		FilenameFilter fileFilter = new FilenameFilter() {    
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".pst")) {
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
					jobTitles.add(line.toLowerCase());
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
	
	public void printMissingProfiles() {
		System.out.println(missedProfiles.keySet().toString());
	}
	public static void main(String args[]) { 
		CommonProfilerStore cProfiles = new CommonProfilerStore();
		/*cProfiles.createProfileStore(System.getProperty("user.dir")+"/models/");
		System.out.println("1: "+cProfiles.findProfile("academic experience"));
		System.out.println("2: "+cProfiles.findProfile("Technical Skills"));*/
		//cProfiles.printMonths();
	}
}
