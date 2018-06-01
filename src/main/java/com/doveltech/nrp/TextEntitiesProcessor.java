package com.doveltech.nrp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.doveltech.nrp.dao.CommonProfilerStore;
import com.doveltech.nrp.utils.FileAccessUtils;
import com.doveltech.nrp.utils.StringRecognitionUtils;



public class TextEntitiesProcessor {
	
	public static void main(String args[]) {        
		String tag = "generic";
		Map<String, List<String>> slider = new HashMap<String, List<String>>();
		
		CommonProfilerStore cProfiles = new CommonProfilerStore();
		cProfiles.createProfileStore(System.getProperty("user.dir")+"/models/");
		try {
			List<String> doc = FileAccessUtils.getFileLineContent("resume.txt");
			for (String line : doc) {
				String[] sent = StringRecognitionUtils.sentenseDetector(line);
				if (sent.length == 1) {
					String profile = null;
					//System.out.println("====> "+sent[0]);
					if ((profile = cProfiles.findProfile(sent[0])) != null) {
						tag = profile;
						if (slider.get(tag) == null)
							slider.put(tag, new ArrayList<String>());
					} else {
						if ((profile = doChunkCheck(cProfiles, sent[0])) != null)
							tag = profile;
						if (slider.get(tag) == null) 
							slider.put(tag, new ArrayList<String>());
						slider.get(tag).add(line);
						
					}
				} else {
					for (int i = 0; i < sent.length; i++) {
						slider.get(tag).add(sent[i]);
					}
				}
			}
			for (Map.Entry<String, List<String>> entry : slider.entrySet()) {
				String key = entry.getKey();
				List<String> values = entry.getValue();	
				System.out.println(key);
				for (String line : values)
					System.out.println("\t"+line);
			}
			cProfiles.createJobTitleData(System.getProperty("user.dir")+"/models/");
			workContentProcessor(cProfiles, slider.get("work_experience"));
			//StringRecognitionUtils.lineChunker(slider.get("work_experience").iterator());
			
			//cProfiles.printMissingProfiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void workContentProcessor(CommonProfilerStore cProfiles, List<String> lines) {
		String cname;
		for (String line : lines) {
			if (line.contains(",")) {
				String[] str = line.split(",");
				for (int i = 0; i < str.length; i++) {
					String token = str[i].replaceAll("\\s+", " ").trim();
					if (cProfiles.isJobTitle(token)) {
						System.out.println("This is the title: "+token);
					} else if ((cname = cProfiles.isCompanyName(token)) != null) {
						System.out.println("Company is: "+cname);
					} else {
						if (token.contains("-"))
							token = token.replaceAll("-", " ");
						String[] tokens = token.split(" ");
						if (cProfiles.isDate(tokens[0])) {
							int from = cProfiles.getMonthPos(tokens[0]);
							if (tokens.length == 4) {
								int year1 = Integer.parseInt(tokens[1]);
								int to = cProfiles.getMonthPos(tokens[2]);
								int year2 = Integer.parseInt(tokens[3]);
								System.out.println("from: "+from+"/"+30+"/"+year1+", to: "+to+"/"+30+"/"+year2);
							}
						}
					}
				}
			} else {
				line = line.replaceAll("\\s+", " ").trim();
				if (cProfiles.isJobTitle(line)) {
					System.out.println("This is the title: "+line);
				} else if ((cname = cProfiles.isCompanyName(line)) != null) {
					System.out.println("Company is: "+cname);
				} else {
					if (line.contains("-"))
						line = line.replaceAll("-", " ");
					line = line.replaceAll("   ", " ");
					String[] tokens = line.split(" ");
					//System.out.println("first index: "+tokens[0]+",len: "+tokens.length);
					if (cProfiles.isDate(tokens[0].trim())) {
						int from = cProfiles.getMonthPos(tokens[0]);
						if (tokens.length == 4) {
							int year1 = Integer.parseInt(tokens[1]);
							int to = cProfiles.getMonthPos(tokens[2]);
							int year2 = Integer.parseInt(tokens[3]);
							System.out.println("from: "+from+"/"+30+"/"+year1+", to: "+to+"/"+30+"/"+year2);
						}
					}  else
						System.out.println("Not known: "+line);
				}
			}
		}
	}
	
	private static String doChunkCheck(CommonProfilerStore cProfiles, String sent) {
		String[] chunks = sent.split(" ");
		String profile;
		
		if (chunks.length < 5) {
			for(int i = 0; i < chunks.length; i++) {
				if ((profile = cProfiles.findProfile(chunks[i].replace(":", ""))) != null) {
					return profile;
				}
			}
		} else {
			if ((profile = cProfiles.findProfile(chunks[0].trim())) != null) 
				return profile;
		}
		return null;
	}    
}
