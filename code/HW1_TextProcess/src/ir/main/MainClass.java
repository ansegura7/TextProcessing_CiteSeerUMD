package ir.main;

/* 
 * Homework: 1
 * author:   Andres Segura
 * date:     June 2018
 * address:  Bogota, Colombia
 * 
 * comments: Main class of the program. Receive the directory path where the text files are located.
 *           It should only be executed and the answers will be obtained.
*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import ir.utility.*;

public class MainClass {
	
	// Class objects
	private static Reader mReader = new Reader();
	private static TextPreprocessing mTP = new TextPreprocessing();
	
	// Main function of main class
	public static void main(String[] args) throws IOException {
		
		// Initial validation
		if (args.length == 0 || !Utilities.filepathExists(args[0])) {
			System.out.println(">> ERROR - The file path does not exists.");
			return;
		}
		
		// Main data structures
		Hashtable<String, String> fileList;
		Hashtable<String, ArrayList<String>> collection;
		Hashtable<String, Hashtable<String, Integer>> invIndex;
		
		// Control variables
		boolean advanced;			// True you want to use the stemmer class and a stopword eliminator
		int nTopWords = 20;			// The maximum number of most frequent words in the collection
		double maxCumFreq = 15.0;	// The maximum percentage of most representative words
		
		/*
		 * 0. Write author info
		 */
		System.out.println("0. Author Info:");
		System.out.println("   Name: Andres Segura Tinoco");
		System.out.println("   Student Code: 201711582");
		System.out.println("   Master: MINE");
		System.out.println("   Approch: First remove the stop-words and then stemming");
		
		/*
		 *  1. Write a program that preprocesses the collection. In doing so, tokenize on whitespace and remove punctuation.
		 */
		System.out.println("\n1. Write a program that preprocesses the collection:");
		String filepath = args[0];
		fileList = mReader.readFiles(filepath);
		System.out.println("   " + fileList.size() + " files were loaded correctly from: " + filepath);
		
		/*
		 * 2. Determine the frequency of occurrence for all the words in the collection. Answer the following questions:
		 */
		System.out.println("\n2. Determine the frequency of occurrence for all the words in the collection:");
		advanced = false;
		collection = mTP.getCollection(fileList, advanced);
		invIndex = preProcessingFiles(collection, nTopWords, maxCumFreq);
		
		/*
		 * 3. Integrate the Porter stemmer and a stopword eliminator into your code. Answer again questions a.-e. from the previous point.
		 */
		System.out.println("\n3. Integrate the Porter stemmer and a stopword eliminator into your code. Answer again questions a.-e.:");
		advanced = true;
		collection = mTP.getCollection(fileList, advanced);
		invIndex = preProcessingFiles(collection, nTopWords, maxCumFreq);
		
		/*
		 * 4. Encode each document using the sparse TF-IDF representation.
		 */
		System.out.println("\n4. Encode each document using the sparse TF-IDF representation:");
		Hashtable<String, ArrayList<String>> sparseIndex = mTP.createSparseTFIDF(invIndex);
		ArrayList<String> wordList = mTP.getOrdedWordList(invIndex);
		createTFIDFoutput(filepath, sparseIndex, wordList);
	}
	  
	/*
	 * Function that pre-processing the content of a collection of documents.
	 */
	 @SuppressWarnings("rawtypes")
	 public static Hashtable<String, Hashtable<String, Integer>> preProcessingFiles(Hashtable<String, ArrayList<String>> collection, int nTopWords, double maxCumFreq) {
		  
		  // Temporal variables
		  String currWord = "";
		  int wordFreq = 0;
		  double wordTotalFreq = 0.0;
		  double cumFreq = 0.0;
		  int minUniqWords = 0;
		  int totalStopWords = 0;
		  int ix = 0;
		  
		  // Get list of stop-words
		  ArrayList<String> stopWords = Utilities.getStopWordList();
		  
		  // Create the inverse index TF-IDF
		  Hashtable<String, Hashtable<String, Integer>> invIndex = mTP.createInverseIndex(collection);
		  
		  // Get sorted list with the most common words on the collection
		  List<Map.Entry> sortedList = mTP.getWordsFrequency(invIndex);
		  
		  /*
		   *  a. What is the total number of words in the collection?
		   */
		  int totalWords = mTP.getWordCount(collection);
		  System.out.println(" a. Total number of words: " + totalWords);
		  
		  /*
		   * b. What is the vocabulary size? (i.e., number of unique terms).
		   */
		  System.out.println(" b. Vocabulary size: " + invIndex.size());
		  
		  /*
		   * c. What are the top 20 words in the ranking? (i.e., the words with the highest frequencies).
		   */
		  System.out.println(" c. Top " + nTopWords + " words in the ranking:");
		  ix = 0;
		  for(Map.Entry entry : sortedList) {
			  ix += 1;
			  currWord = (String) entry.getKey();
			  wordFreq = (int) entry.getValue();
			  wordTotalFreq = ((100.0 * wordFreq) / totalWords);
			  
			  // Show only top N words
			  if (ix <= nTopWords) {
				  System.out.println("    The word(" + ix + ") '" + currWord + "' has a frequency of: " + wordFreq + " and an absolute frequency of: " + wordTotalFreq + "%");
			  }
			  
			  // Calculating the minimum number of unique words accounting for 15% of the total number of words in the collection
			  if (cumFreq < maxCumFreq) {
				  cumFreq += wordTotalFreq;
				  minUniqWords++;
			  }
		  }
		  
		  /*
		   * d. From these top 20 words, which ones are stop-words?
		   */
		  System.out.println(" d. List of stop-words contained in the top " + nTopWords + " words:");
		  ix = 0;
		  for(Map.Entry entry : sortedList) {
			  ix += 1;
			  
			  if (ix <= nTopWords) {
				  currWord = (String) entry.getKey();
				  
				  if (stopWords.contains(currWord)) {
					  totalStopWords++;
					  System.out.println("    The word(" + ix + ") '" + currWord + "' is a stop-word.");
				  }
			  }
			  else
				  break;
		  }
		  System.out.println("    The top " + nTopWords + " words (most common) that are stopwords is: " + totalStopWords);
		  
		  /*
		   * e. What is the minimum number of unique words accounting for 15% of the total number of words in the collection?
		   */
		  System.out.println(" e. The minimum number of unique words accounting for 15% is:");
		  System.out.println("    " + minUniqWords + " which represent the " + cumFreq + " % of the total number of words in the collection.");
		  
		  // Return inverse-index
		  return invIndex;
	 }
	 
	 /*
	  * Save into text files the output of the TF-IDF.
	  */
	 private static void createTFIDFoutput(String filepath, Hashtable<String, ArrayList<String>> sparseIndex, ArrayList<String> wordList) {
		 ArrayList<String> fileList = new ArrayList<String>();
		 ArrayList<String> tfidf = new ArrayList<String>();
		 String filename = "";
		 
		 // Temporal variables
		 Enumeration<String> e = sparseIndex.keys();
		 String doctName = "";
		 String doctData = "";
		 int ix = 0;
		 int nShow = 10;
		 
		 // Iteration
		 System.out.println(" - First " + nShow + " examples:");
		 while(e.hasMoreElements()) {
			 
			 // Get data
			 doctName = e.nextElement();
			 doctData = sparseIndex.get(doctName).toString();
			 
			 // Save data
			 fileList.add(doctName);
			 tfidf.add(doctData);
			 
			 // Show some data
			 if (++ix <= nShow) {
				 System.out.println("   " + ix + ": " + doctName + " - " + doctData);
			 }
		 }
		 
		 // Output file path
		 System.out.println("\n - Output files:");
		 filepath = filepath + "../output/";
		 
		 // Create file list output
		 filename = filepath + "filelist.txt";
		 mReader.saveArrayList(fileList, filename);
		 System.out.println("   File list output: " + filename);
		 
		 // Create word list output
		 filename = filepath + "wordlist.txt";
		 mReader.saveArrayList(wordList, filename);
		 System.out.println("   Word list output: " + filename);
		 
		 // Create sparse index output
		 filename = filepath + "sparse-tf-idf.txt";
		 mReader.saveArrayList(tfidf, filename);
		 System.out.println("   Sparse TF-IDF representation: " + filename);
		 
	 }
}