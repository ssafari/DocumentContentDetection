package com.doveltech.nrp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.doveltech.dao.CommonProfilerStore;
import com.doveltech.dao.FileAccessUtils;
import com.doveltech.nrp.utils.StringRecognitionUtils;



public class TextEntitiesProcessor {
	public static final String sentence = "Mike is senior programming manager and Jon is a clerk both are working at Tutorialspoint"; 
	
	
	public static void main(String args[]) {        
		String tag = "generic";
		Map<String, List<String>> slider = new HashMap<String, List<String>>();
		
		CommonProfilerStore cProfiles = new CommonProfilerStore();
		cProfiles.createProfileStore(System.getProperty("user.dir")+"/models/");
		//cProfiles.printProfiles();
		try {
			List<String> doc = FileAccessUtils.getBufferLine(FileAccessUtils.retrieveFile("resume.txt"));
			for (String line : doc) {
				String[] sent = StringRecognitionUtils.sentenseDetector(line);
				if (sent.length == 1) {
					String profile = null;
					if ((profile = cProfiles.findProfile(sent[0])) != null) {
						//System.out.println(profile);
						tag = profile;
						if (slider.get(tag) == null)
							slider.put(tag, new ArrayList<String>());
					} else {
						if (slider.get(tag) == null)
							slider.put(tag, new ArrayList<String>());
						slider.get(tag).add(line);
					}
				} else {
					for (int i = 0; i < sent.length; i++) {
						//System.out.println("line nb ["+i+"]: "+sent[i]);
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
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
}
