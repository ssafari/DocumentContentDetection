package com.doveltech.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

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
		Pattern p = Pattern.compile("[a-zA-Z0-9]");
		FileInputStream fis = new FileInputStream(file);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		List<String> doc = new ArrayList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			if (!line.isEmpty() && p.matcher(line).find()) {
				System.out.println(line);
				doc.add(line.trim().toLowerCase());
			}
		}
		br.close();
		return doc;
	}
	public static List<String> getBufferLine(String file) throws IOException {
		
		Pattern p = Pattern.compile("[a-zA-Z0-9]");
				
		FileInputStream fis = new FileInputStream(getModelFile(file));
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		List<String> doc = new ArrayList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			if (!line.isEmpty() && p.matcher(line).find()) {
				//System.out.println(line);
				doc.add(line.trim());
			}
		}
		br.close();
		return doc;
	}
	
	public static File parseToHTMLUsingApacheTikka(String file)
			throws IOException, SAXException, TikaException {
		// determine extension
		String ext = FilenameUtils.getExtension(file);
		String outputFileFormat = "";
		// ContentHandler handler;
		if (ext.equalsIgnoreCase("html") | ext.equalsIgnoreCase("pdf")
				| ext.equalsIgnoreCase("doc") | ext.equalsIgnoreCase("docx")) {
			outputFileFormat = ".html";
			// handler = new ToXMLContentHandler();
		} else if (ext.equalsIgnoreCase("txt") | ext.equalsIgnoreCase("rtf")) {
			outputFileFormat = ".txt";
		} else {
			System.out.println("Input format of the file " + file
					+ " is not supported.");
			return null;
		}
		String OUTPUT_FILE_NAME = FilenameUtils.removeExtension(file)
				+ outputFileFormat;
		ContentHandler handler = new ToXMLContentHandler();
		// ContentHandler handler = new BodyContentHandler();
		// ContentHandler handler = new BodyContentHandler(
		// new ToXMLContentHandler());
		InputStream stream = new FileInputStream(file);
		AutoDetectParser parser = new AutoDetectParser();
		Metadata metadata = new Metadata();
		try {
			parser.parse(stream, handler, metadata);
			FileWriter htmlFileWriter = new FileWriter(OUTPUT_FILE_NAME);
			htmlFileWriter.write(handler.toString());
			htmlFileWriter.flush();
			htmlFileWriter.close();
			return new File(OUTPUT_FILE_NAME);
		} finally {
			stream.close();
		}
}
}
