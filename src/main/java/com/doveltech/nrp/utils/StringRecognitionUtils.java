package com.doveltech.nrp.utils;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

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

public class StringRecognitionUtils {

	
	/**
	 * 
	 * @param wholeFile
	 * @return
	 * @throws IOException 
	 */
	public static String[] sentenseProvider(String file) throws IOException {

		SentenceModel model = new SentenceModel(FileAccessUtils.retrieveFile("en-sent.bin"));
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		String[] res = sentenceDetector.sentDetect(FileAccessUtils.readFileToString(file));
		//for (int i = 0; i < res.length; i++)
		//	System.out.println("line nb ["+i+"]: "+res[i]);
		
		return res;
	}
	
	
	public static String[] sentenseDetector(String phrase) throws IOException {
		SentenceModel model = new SentenceModel(FileAccessUtils.retrieveFile("en-sent.bin"));
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		String[] res = sentenceDetector.sentDetect(phrase);
		return res;
	}
	
	/**
	 * 
	 * @param sentence
	 */
	public static void personFinder(String sentence) {
		//Loading the tokenizer model 
		TokenizerModel tokenModel = new TokenizerModelLoader().load(FileAccessUtils.retrieveFile("en-token.bin"));

		//Instantiating the TokenizerME class 
		TokenizerME tokenizer = new TokenizerME(tokenModel); 

		//Tokenizing the sentence in to a string array 
		String tokens[] = tokenizer.tokenize(sentence); 

		//Loading the NER-person model 
		TokenNameFinderModel nameModel = new TokenNameFinderModelLoader().load(FileAccessUtils.retrieveFile("en-ner-person.bin"));

		//Instantiating the NameFinderME class 
		NameFinderME nameFinder = new NameFinderME(nameModel);       

		//Finding the names in the sentence 
		Span nameSpans[] = nameFinder.find(tokens);        

		//Printing the names and their spans in a sentence 
		for(Span s: nameSpans)        
			System.out.println(s.toString()+"  "+tokens[s.getStart()]); 
	}
	public static void lineChunker(Iterator<String> iter) {
		String whitespaceTokenizerLine[] = null;
		String[] tags = null;
		
		ChunkerModel cModel = new ChunkerModelLoader().load(FileAccessUtils.retrieveFile("en-chunker.bin"));
		POSModel model = new POSModelLoader().load(FileAccessUtils.retrieveFile("en-pos-maxent.bin"));
		POSTaggerME tagger = new POSTaggerME(model);
		
		while (iter.hasNext()) {
			whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(iter.next());
			tags = tagger.tag(whitespaceTokenizerLine);
			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
			System.out.println(sample.toString());
			
		}
		ChunkerME chunkerME = new ChunkerME(cModel);
		String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);
	 
		for (String s : result)
			System.out.println(s);
	 
		/*Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
		for (Span s : span)
			System.out.println(s.toString());*/
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void chunk(String fileName) throws IOException {
		String line;
		String whitespaceTokenizerLine[] = null;
		String[] tags = null;
		
		POSModel model = new POSModelLoader().load(FileAccessUtils.retrieveFile("en-pos-maxent.bin"));
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = new POSTaggerME(model);
	 
		InputStreamFactory isf = new MarkableFileInputStreamFactory(FileAccessUtils.fileFormatHandler(fileName));
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
		ChunkerModel cModel = new ChunkerModelLoader().load(FileAccessUtils.retrieveFile("en-chunker.bin"));
	 
		ChunkerME chunkerME = new ChunkerME(cModel);
		String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);
	 
		for (String s : result)
			System.out.println(s);
	 
		Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
		for (Span s : span)
			System.out.println(s.toString());
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public static void POSTag(String line) throws IOException {
		
		POSModel model = new POSModelLoader().load(FileAccessUtils.retrieveFile("en-pos-maxent.bin"));
		POSTaggerME tagger = new POSTaggerME(model);
		String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(line);
		String[] tags = tagger.tag(whitespaceTokenizerLine);
		POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
		System.out.println(sample.getSentence()[0]);
		
	}
	
	public static void main(String args[]) { 
		try {
			chunk("Resume_Federal_Erika_Ogilvy.pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
