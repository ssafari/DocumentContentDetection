package com.doveltech.nrp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.chunker.ChunkerModelLoader;
import opennlp.tools.cmdline.namefind.TokenNameFinderModelLoader;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.cmdline.tokenizer.TokenizerModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;



public class TextEntitiesProcessor {
	public static final String sentence = "Mike is senior programming manager and Jon is a clerk both are working at Tutorialspoint"; 
	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static String readFileToString(File name) {
		//File file = new File("D:\\path\\report.html");
		try {
			String str = FileUtils.readFileToString(name, "UTF-8");
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param wholeFile
	 * @return
	 */
	public static String[] sentenseProvider(String file) {

		try {
			SentenceModel model = new SentenceModel(getModelFile("en-sent.bin"));
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
			return sentenceDetector.sentDetect(readFileToString(getModelFile(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void chunk() throws IOException {
		String line;
		String whitespaceTokenizerLine[] = null;
		String[] tags = null;
		
		POSModel model = new POSModelLoader().load(getModelFile("en-pos-maxent.bin"));
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = new POSTaggerME(model);
	 
		
		InputStreamFactory isf = new MarkableFileInputStreamFactory(getModelFile("resume.txt"));
		ObjectStream<String> lineStream = new PlainTextByLineStream(isf, Charset.forName("UTF-8"));
	 
		perfMon.start();
		while ((line = lineStream.read()) != null) {
			whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(line);
			tags = tagger.tag(whitespaceTokenizerLine);
	 
			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
			System.out.println(sample.toString());
				perfMon.incrementCounter();
		}
		perfMon.stopAndPrintFinalResult();
	 
		// chunker
		ChunkerModel cModel = new ChunkerModelLoader().load(getModelFile("en-chunker.bin"));
	 
		ChunkerME chunkerME = new ChunkerME(cModel);
		String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);
	 
		for (String s : result)
			System.out.println(s);
	 
		Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
		for (Span s : span)
			System.out.println(s.toString());
	}
	
	/*public static void POSTag() throws IOException {
		POSModel model = new POSModelLoader()	
			.load(new File("en-pos-maxent.bin"));
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = new POSTaggerME(model);
	 
		String input = "Hi. How are you? This is Mike.";
		ObjectStream<String> lineStream = new PlainTextByLineStream(new StringReader(input));
	 
		perfMon.start();
		String line;
		while ((line = lineStream.read()) != null) {
	 
			String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
					.tokenize(line);
			String[] tags = tagger.tag(whitespaceTokenizerLine);
	 
			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
			System.out.println(sample.toString());
	 
			perfMon.incrementCounter();
		}
		perfMon.stopAndPrintFinalResult();
	}*/
	
	public static File getModelFile(String model) {
		String path = System.getProperty("user.dir")+"/models/"+model;
		System.out.println(path);
		return new File(path);
	}
	
	public static void personFinder() {
		//Loading the tokenizer model 
		TokenizerModel tokenModel = new TokenizerModelLoader().load(getModelFile("en-token.bin"));

		//Instantiating the TokenizerME class 
		TokenizerME tokenizer = new TokenizerME(tokenModel); 

		//Tokenizing the sentence in to a string array 
		String tokens[] = tokenizer.tokenize(sentence); 

		//Loading the NER-person model 
		TokenNameFinderModel nameModel = new TokenNameFinderModelLoader().load(getModelFile("en-ner-person.bin"));

		//Instantiating the NameFinderME class 
		NameFinderME nameFinder = new NameFinderME(nameModel);       

		//Finding the names in the sentence 
		Span nameSpans[] = nameFinder.find(tokens);        

		//Printing the names and their spans in a sentence 
		for(Span s: nameSpans)        
			System.out.println(s.toString()+"  "+tokens[s.getStart()]); 
	}
	
	
	public static void main(String args[]) {        
	      //personFinder();
		String[] sentenses = sentenseProvider("resume.txt");
		for (int i = 0; i < sentenses.length; i++)
			System.out.println("line nb ["+i+"]: "+sentenses[i]);
		
		try {
			chunk();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
	
}
