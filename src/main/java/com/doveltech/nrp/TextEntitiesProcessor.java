package com.doveltech.nrp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.doveltech.nrp.dao.CommonProfilerStore;
import com.doveltech.nrp.dao.Company;
import com.doveltech.nrp.dao.Resume;
import com.doveltech.nrp.utils.FileAccessUtils;
import com.doveltech.nrp.utils.StringRecognitionUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class TextEntitiesProcessor {
	
	private static Resume rme = new Resume();
	
	public static void main(String args[]) {        
		String tag = "generic";
		Map<String, List<String>> slider = new HashMap<String, List<String>>();
		
		CommonProfilerStore cProfiles = new CommonProfilerStore();
		cProfiles.createProfileStore(System.getProperty("user.dir")+"/models/");
		try {
			List<String> doc = FileAccessUtils.getFileLineContent("Resume_Sydney_Taylor.txt");
			for (String line : doc) {
				String[] sent = StringRecognitionUtils.sentenseDetector(line);
				if (sent.length == 1) {
					String profile = null;
					
					sent[0] = sent[0].replaceAll("\\s+", " ").trim();
					if (sent[0].endsWith(":"))
						sent[0] = sent[0].replace(":", "");
					System.out.println("1====> "+sent[0]);
					if ((profile = cProfiles.findProfile(sent[0])) != null) {
						tag = profile;
						if (slider.get(tag) == null) {
							slider.put(tag, new ArrayList<String>());
							continue;
						}
					} else {
						String[] words = sent[0].split(" ");
						if (words.length < 5) {
							for(int i = 0; i < words.length; i++) {
								if ((profile = cProfiles.findProfile(words[i])) != null) {
									tag = profile;
									if (slider.get(tag) == null) {
										slider.put(tag, new ArrayList<String>());
										continue;
									}
								}
							}
						}
					}
					if (slider.get(tag) == null) 
						slider.put(tag, new ArrayList<String>());
					slider.get(tag).add(sent[0]);
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
			workExpStripper(cProfiles, slider.get("work_experience"));
			skillsStripper(cProfiles, slider.get("skills"));
			personalInfoStripper(cProfiles, slider.get("generic"));
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(rme);
			System.out.println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void personalInfoStripper(CommonProfilerStore cProfiles, List<String> lines) {
		String address = "";
		String[] raws = lines.toArray(new String[0]);
		rme.setFullName(raws[0].trim());
		System.out.println("This is the person: "+raws[0]);
		
		for (int i =1; i < raws.length; i++) {
			if (raws[i].startsWith("www.")) {
				System.out.println("This is the link: "+raws[i]);
				continue;
			}
			if (raws[i].contains(":")) {
				String[] tokens = raws[i].split(":");
				String ind = tokens[0].toLowerCase().trim();
				if (ind.contentEquals("tel") || ind.contentEquals("phone") || ind.contentEquals("cell")) {
					rme.setPhone(tokens[1].toLowerCase().trim());
					System.out.println("This is phone: "+tokens[1].toLowerCase().trim());
				} else if (ind.contentEquals("email")) {
					rme.setEmail(tokens[1].toLowerCase().trim());
					System.out.println("This is email: "+tokens[1].toLowerCase().trim());
				} else if (ind.contentEquals("address")) {
					address = address+tokens[1].toLowerCase().trim();
				} else if (ind.contentEquals("web") || ind.contentEquals("website") || ind.contentEquals("linkedin")) {
					System.out.println("This is the link: "+tokens[1].toLowerCase().trim());
				}
			} else {
				if (raws[i].contains("@")) {
					rme.setEmail(raws[i].trim());
					System.out.println("This is email: "+raws[i]);
				}
				if (raws[i].contains("-")) {
					String str = raws[i].replaceAll("-", "");
					str = str.replaceAll("\\s+", "").trim();
					if (StringUtils.isNumeric(str)) {
						rme.setPhone(raws[i]);
						System.out.println("This is phone: "+raws[i]);
					}
				}
				address = address+raws[i];
			}
		}
		System.out.println("This is addr: "+address);
		rme.setAddress(address);
	}
	
	private static void skillsStripper(CommonProfilerStore cProfiles, List<String> lines) {
		String token;
		
		for (String line : lines) {
			if (line.contains(":")) {
				String[] str = line.split(":");
				if (str[1].contains(",")) {
					String[] ln = str[1].split(",");
					for (int i = 0; i < ln.length; i++) {
						token = ln[i].replaceAll("\\s+", " ").trim();
						rme.setSkills(token);
					}
				} else {
					rme.setSkills(str[1]);
				}
			} else {
				if (line.contains(",")) {
					String[] ln = line.split(",");
					for (int i = 0; i < ln.length; i++) {
						token = ln[i].replaceAll("\\s+", " ").trim();
						rme.setSkills(token);
					}
				} else {
					rme.setSkills(line);
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static void workExpStripper(CommonProfilerStore cProfiles, List<String> lines) {
		String cname = null;
		String sdate = null;
		String edate = null;
		String title = null;
		
		
		for (String line : lines) {
			if (line.contains(",")) {
				String[] str = line.split(",");
				for (int i = 0; i < str.length; i++) {
					String token = str[i].replaceAll("\\s+", " ").trim();
					if (cProfiles.isJobTitle(token)) {
						System.out.println("This is the title: "+token);
						title = token;
					} else if (cProfiles.isCompanyName(token) != null) {
						cname = cProfiles.isCompanyName(token);
						Company co = rme.findCompany(cname.trim());
						if (co == null) {
							Company new_co = new Company(cname.trim(), title, edate, sdate);
							rme.setCompanies(new_co);
							cname = null;
							if (title != null)
								title = null;
						}
						System.out.println("Company is: "+cname);
					} else {
						if (token.contains("-"))
							token = token.replaceAll("-", " ");
						token = token.replaceAll("\\s+", " ").trim();
						String[] tokens = token.split(" ");
						if (cProfiles.isDate(tokens[0])) {
							int from = cProfiles.getMonthPos(tokens[0]);
							if (tokens.length == 4) {
								int year1 = Integer.parseInt(tokens[1]);
								int to = cProfiles.getMonthPos(tokens[2]);
								int year2 = Integer.parseInt(tokens[3]);
								System.out.println("from: "+from+"/"+30+"/"+year1+", to: "+to+"/"+30+"/"+year2);
								sdate = from+"/"+30+"/"+year1;
								edate = to+"/"+30+"/"+year2;
								if (cname != null) {
									Company co = rme.findCompany(cname.trim());
									System.out.println("(1) sdate: "+sdate);
									if (co != null) {
										co.setSdate(sdate);
										co.setEdate(edate);
										sdate = null;
										edate = null;
									}
								}
							}
						}
					}
				}
			} else {
				line = line.replaceAll("\\s+", " ").trim();
				if (cProfiles.isJobTitle(line)) {
					System.out.println("This is the title: "+line);
					title = line;
				} else if (cProfiles.isCompanyName(line) != null) {
					cname = cProfiles.isCompanyName(line);
					Company co = rme.findCompany(cname.trim());
					if (co == null) {
						Company new_co = new Company(cname.trim(), title, edate, sdate);
						rme.setCompanies(new_co);
					}
					System.out.println("Company is: "+cname);
				} else {
					if (line.contains("-"))
						line = line.replaceAll("-", " ");
					line = line.replaceAll("   ", " ");
					String[] tokens = line.split(" ");
					if (cProfiles.isDate(tokens[0].trim())) {
						int from = cProfiles.getMonthPos(tokens[0]);
						if (tokens.length == 4) {
							int year1 = Integer.parseInt(tokens[1]);
							int to = cProfiles.getMonthPos(tokens[2]);
							int year2 = Integer.parseInt(tokens[3]);
							System.out.println("from: "+from+"/"+30+"/"+year1+", to: "+to+"/"+30+"/"+year2);
							sdate = from+"/"+30+"/"+year1;
							edate = to+"/"+30+"/"+year2;
							if (cname != null) {
								Company co = rme.findCompany(cname.trim());
								System.out.println("(1) sdate: "+sdate);
								if (co != null) {
									co.setSdate(sdate);
									co.setEdate(edate);
								}
							}
						}
					}  else {
						System.out.println("Not known: "+line);
					}
				}
			}
		}
	}
}
