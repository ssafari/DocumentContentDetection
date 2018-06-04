package com.doveltech.nrp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;



/**
 * 
 * @author sep
 *
 */
public final class FileAccessUtils {

	/**
	 * 
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	public static String readFileToString(String name) throws IOException {
		String str = FileUtils.readFileToString(retrieveFile(name), "UTF-8");
		return str;
	}
	
	public static File retrieveFile(String filename) {
		String path = System.getProperty("user.dir")+"/models/"+filename;
		//System.out.println(path);
		return new File(path);
	}
	
	public static List<String> getFileLineContent(String name) throws IOException {
		File file = fileFormatHandler(name);
		FileInputStream fis = new FileInputStream(file);
		Pattern p = Pattern.compile("[a-zA-Z0-9]");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		List<String> doc = new ArrayList<String>();
		
		String line = null;
		while ((line = br.readLine()) != null) {
			if (p.matcher(line).find()) {
				String[] tokens = line.split("      ");
				for (int i=0; i < tokens.length; i++) {
					String st = tokens[i].replaceAll("[^A-Za-z0-9,-@\\.\\w]", " ");
					st = st.trim();
					if (!st.isEmpty()) {
						//System.out.println("==>"+st.trim());
						doc.add(st);
					}
				}
			}
		}
		br.close();
		return doc;
	}
	
	public static File fileFormatHandler(String filename) throws FileNotFoundException, IOException {
		String ext = FilenameUtils.getExtension(filename);
		final String TextFileFormat = ".txt";
		
		if (ext.equalsIgnoreCase("txt"))
			return retrieveFile(filename);
		
		// File format ContentHandler;
		String new_format = FilenameUtils.removeExtension(filename)+ TextFileFormat;
		switch (ext.toLowerCase()) {
		case "pdf":
			PDFtoText(retrieveFile(filename), System.getProperty("user.dir")+"/models/"+new_format);
			break;
		case "doc":

			break;
		case "docx":

			break;
		case "rtf":

			break;
		default:
			System.out.println("Input format of the file " + filename
					+ " is not supported.");
			return null;
		}
		
		return retrieveFile(new_format);
	}
	
	public static void PDFtoText(File f, String name) throws FileNotFoundException, IOException {
		PDFParser parser = new PDFParser(new RandomAccessFile(f, "r"));
		parser.parse();
		PDDocument pdDocument = new PDDocument(parser.getDocument());
		PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
		String string = pdfTextStripper.getText(pdDocument);
		PrintWriter pw = new PrintWriter(name);
		pw.print(string);
		pw.close();
	}
	
	public static List<String> PDFStripper(String full_path) throws InvalidPasswordException, IOException {
		PDDocument document = PDDocument.load(new File(full_path));

		document.getClass();

		if (!document.isEncrypted()) {
			PDFParser parser = new PDFParser(new RandomAccessFile(new File(full_path), "r"));
			parser.parse();
						
			PDDocument pdDocument = new PDDocument(parser.getDocument());
			PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
			String pdfFileInText = pdfTextStripper.getText(pdDocument);

			Pattern p = Pattern.compile("[a-zA-Z0-9]");
			List<String> doc = new ArrayList<String>();
			
			// split by whitespace
			String lines[] = pdfFileInText.split("\\r?\\n");
			for (String line : lines) {
				if (p.matcher(line).find()) {
					String[] tokens = line.split("      ");
					for (int i=0; i < tokens.length; i++) {
						String st = tokens[i].trim();
						if (!st.isEmpty()) {
							System.out.println("==>"+st);
							doc.add(st.replaceAll("[^A-Za-z0-9,-@\\. ]", ""));
						}
					}
				}
			}
			return doc;
		}
		return null;
	}
	
	public static void main(String args[]) { 
		try {
			PDFStripper(System.getProperty("user.dir")+"/models/Resume_Marcos_Silva.pdf");  //Resume_Marcos_Silva.pdf
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
